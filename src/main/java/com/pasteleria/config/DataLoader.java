package com.pasteleria.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.model.*;
import com.pasteleria.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        // Only load data if database is empty
        if (userRepository.count() == 0) {
            logger.info("Database is empty. Loading initial data from JSON files...");
            loadUsers();
            loadProductsAndCategories();

            logger.info("Initial data loaded successfully!");
        } else {
            logger.info("Database already contains data. Skipping data load.");
        }

        // Load blogs independently if missing
        loadBlogs();

        // Ensure SuperAdmin password is correct (fix for login issues)
        fixSuperAdminPassword();
    }

    private void fixSuperAdminPassword() {
        userRepository.findByCorreo("ana@duoc.cl").ifPresent(user -> {
            user.setPassword(passwordEncoder.encode("duoc123"));
            userRepository.save(user);
            logger.info("Reset password for ana@duoc.cl to default");
        });
    }

    private void loadUsers() throws Exception {
        logger.info("Loading users from usuarios.json...");

        InputStream inputStream = new ClassPathResource("data/usuarios.json").getInputStream();
        List<JsonNode> usersJson = objectMapper.readValue(inputStream, new TypeReference<List<JsonNode>>() {
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Default password for all users: "duoc123"
        String defaultPassword = passwordEncoder.encode("duoc123");

        for (JsonNode userJson : usersJson) {
            User user = new User();
            user.setRun(userJson.get("run").asText());
            user.setDv(userJson.has("dv") ? userJson.get("dv").asText() : null);
            user.setNombre(userJson.get("nombre").asText());
            user.setApellidos(userJson.get("apellidos").asText());
            user.setCorreo(userJson.get("correo").asText());

            if (userJson.has("fechaNacimiento")) {
                user.setFechaNacimiento(LocalDate.parse(userJson.get("fechaNacimiento").asText(), formatter));
            }

            user.setCodigoDescuento(userJson.has("codigoDescuento") ? userJson.get("codigoDescuento").asText() : null);

            // Map user type
            String tipoUsuario = userJson.get("tipoUsuario").asText();
            user.setTipoUsuario(mapRole(tipoUsuario));

            user.setRegionId(userJson.get("regionId").asText());
            user.setRegionNombre(userJson.get("regionNombre").asText());
            user.setComuna(userJson.get("comuna").asText());
            user.setDireccion(userJson.get("direccion").asText());
            user.setPassword(defaultPassword);
            user.setAvatarUrl(userJson.has("avatarUrl") ? userJson.get("avatarUrl").asText() : null);
            user.setActivo(userJson.has("activo") ? userJson.get("activo").asBoolean() : true);

            userRepository.save(user);
            logger.info("Loaded user: {} ({})", user.getCorreo(), user.getTipoUsuario());
        }

        logger.info("Loaded {} users", usersJson.size());
    }

    private Role mapRole(String tipoUsuario) {
        switch (tipoUsuario) {
            case "SuperAdmin":
                return Role.ROLE_SUPERADMIN;
            case "Administrador":
                return Role.ROLE_ADMIN;
            case "Vendedor":
                return Role.ROLE_SELLER;
            default:
                return Role.ROLE_CUSTOMER;
        }
    }

    private void loadProductsAndCategories() throws Exception {
        logger.info("Loading categories and products from menu_datos.json...");

        InputStream inputStream = new ClassPathResource("data/menu_datos.json").getInputStream();
        JsonNode menuData = objectMapper.readTree(inputStream);

        JsonNode categoriasJson = menuData.get("categorias");

        for (JsonNode categoriaJson : categoriasJson) {
            Categoria categoria = new Categoria();
            categoria.setNombre(categoriaJson.get("nombre_categoria").asText());
            categoria.setProductos(new ArrayList<>());

            // Save category first
            categoria = categoriaRepository.save(categoria);
            logger.info("Loaded category: {}", categoria.getNombre());

            // Load products for this category
            JsonNode productosJson = categoriaJson.get("productos");
            for (JsonNode productoJson : productosJson) {
                Producto producto = new Producto();
                producto.setCodigoProducto(productoJson.get("codigo_producto").asText());
                producto.setNombre(productoJson.get("nombre_producto").asText());
                producto.setPrecio(productoJson.get("precio_producto").asDouble());
                producto.setDescripcion(productoJson.get("descripción_producto").asText());
                producto.setImagenPrincipal(productoJson.get("imagen_producto").asText());

                // Convert images array to JSON string
                JsonNode imagenesDetalle = productoJson.get("imagenes_detalle");
                producto.setImagenesDetalle(objectMapper.writeValueAsString(imagenesDetalle));

                producto.setStock(productoJson.get("stock").asInt());
                producto.setStockCritico(productoJson.get("stock_critico").asInt());
                producto.setCategoria(categoria);

                productoRepository.save(producto);
                logger.info("  - Loaded product: {} ({})", producto.getNombre(), producto.getCodigoProducto());
            }
        }

        logger.info("Loaded {} categories with products", categoriasJson.size());
    }

    private void loadBlogs() {
        logger.info("Loading initial blog...");
        if (blogRepository.count() == 0) {
            Blog blog = new Blog();
            blog.setTitulo("Bienvenidos al Blog de Mil Sabores");
            blog.setDescripcion(
                    "Este es un artículo de ejemplo creado automáticamente para mostrar cómo funciona el blog.");
            blog.setContenido(
                    "En Mil Sabores compartimos recetas, tips, novedades y trucos de pastelería. Este post sirve como demostración inicial.");
            blog.setAutorId("sistema");
            blog.setAutorNombre("Pastelería Mil Sabores");
            blog.setPortada(
                    "https://res.cloudinary.com/dx83p4455/image/upload/v1762268069/cheesecake_sin_azucar_dggxns.png");
            blog.setStatus("aprobado");

            blogRepository.save(blog);
            logger.info("Loaded initial blog: {}", blog.getTitulo());
        } else {
            // Fix broken image if exists (migration fix)
            List<Blog> blogs = blogRepository.findAll();
            for (Blog blog : blogs) {
                if ("/images/blog/default-cover.jpg".equals(blog.getPortada())) {
                    blog.setPortada(
                            "https://res.cloudinary.com/dx83p4455/image/upload/v1762268069/cheesecake_sin_azucar_dggxns.png");
                    blogRepository.save(blog);
                    logger.info("Updated blog cover for: {}", blog.getTitulo());
                }
            }
        }
    }
}
