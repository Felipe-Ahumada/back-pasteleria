# Backend Pastelería Mil Sabores

API REST para la gestión de Pastelería Mil Sabores desarrollada con Spring Boot 3.5.0, JWT Authentication, y MySQL.

## Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- MySQL 8.0+ (XAMPP recomendado)
- Node.js y npm (para el frontend)

## Configuración Inicial

### 1. Configurar Base de Datos (XAMPP)

1. Asegúrate de que XAMPP esté ejecutándose with MySQL iniciado
2. Crea la base de datos ejecutando:

```bash
mysql -u root -p
```

Luego ejecuta:

```sql
CREATE DATABASE pasteleria_db;
USE pasteleria_db;
```

O ejecuta el script SQL proporcionado:

```bash
mysql -u root < database/schema.sql
```

### 2. Compilar y Ejecutar el Backend

```bash
# Compilar el proyecto
./mvnw clean compile

# Ejecutar la aplicación
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### 3. Acceder a Swagger UI

Una vez que la aplicación esté ejecutándose, accede a la documentación de la API en:

```
http://localhost:8080/doc/swagger-ui.html
```

## Endpoints Principales

### Autenticación (Públicos)

- `POST /api/v1/auth/login` - Iniciar sesión
- `POST /api/v1/auth/register` - Registrar usuario
- `GET /api/v1/auth/me` - Obtener usuario actual (requiere token)

### Productos (Lectura pública, escritura protegida)

- `GET /api/v1/productos` - Listar todos los productos
- `GET /api/v1/productos/{id}` - Obtener producto por ID
- `GET /api/v1/productos/categoria/{categoriaId}` - Productos por categoría
- `POST /api/v1/productos` - Crear producto (ADMIN/SELLER)
- `PUT /api/v1/productos/{id}` - Actualizar producto (ADMIN/SELLER)
- `DELETE /api/v1/productos/{id}` - Eliminar producto (ADMIN)

### Categorías (Lectura pública, escritura ADMIN)

- `GET /api/v1/categorias` - Listar categorías
- `POST /api/v1/categorias` - Crear categoría (ADMIN)
- `PUT /api/v1/categorias/{id}` - Actualizar categoría (ADMIN)
- `DELETE /api/v1/categorias/{id}` - Eliminar categoría (ADMIN)

### Pedidos (Autenticados)

- `POST /api/v1/pedidos` - Crear pedido
- `GET /api/v1/pedidos` - Mis pedidos
- `GET /api/v1/pedidos/all` - Todos los pedidos (ADMIN/SELLER)
- `PUT /api/v1/pedidos/{id}/estado` - Actualizar estado (ADMIN/SELLER)

### Usuarios (ADMIN)

- `GET /api/v1/users` - Listar usuarios (ADMIN)
- `GET /api/v1/users/{id}` - Obtener usuario (ADMIN o propio)
- `PUT /api/v1/users/{id}` - Actualizar usuario (ADMIN o propio)
- `DELETE /api/v1/users/{id}` - Desactivar usuario (ADMIN)

## Roles y Permisos

- **ROLE_SUPERADMIN**: Acceso total al sistema
- **ROLE_ADMIN**: Gestión completa de productos, categorías, usuarios y pedidos
- **ROLE_SELLER**: Gestión de productos y pedidos
- **ROLE_CUSTOMER**: Ver productos, crear y ver sus propios pedidos

## Autenticación JWT

El sistema utiliza tokens JWT para autenticación. Para usar endpoints protegidos:

1. Obtén un token mediante POST `/api/v1/auth/login`:

```json
{
  "email": "ana@duoc.cl",
  "password": "tu_password"
}
```

2. Incluye el token en el header de las siguientes peticiones:

```
Authorization: Bearer <tu_token_jwt>
```

3. El token expira en 24 horas (configurable en `application.properties`)

## Estructura del Proyecto

```
src/main/java/com/pasteleria/
├── BackPasteleriaApplication.java  # Clase principal
├── config/
│   ├── SecurityConfig.java         # Configuración de seguridad
│   └── SwaggerConfig.java          # Configuración de Swagger
├── controller/
│   ├── AuthController.java         # Endpoints de autenticación
│   ├── ProductoController.java     # CRUD de productos
│   ├── CategoriaController.java    # CRUD de categorías
│   ├── PedidoController.java       # Gestión de pedidos
│   └── UserController.java         # Gestión de usuarios
├── dto/
│   ├── LoginRequest.java           # DTO para login
│   ├── LoginResponse.java          # DTO de respuesta login
│   └── UserResponse.java           # DTO de usuario (sin password)
├── model/
│   ├── User.java                   # Entidad Usuario
│   ├── Role.java                   # Enum de roles
│   ├── Categoria.java              # Entidad Categoría
│   ├── Producto.java               # Entidad Producto
│   ├── Pedido.java                 # Entidad Pedido
│   └── PedidoItem.java             # Entidad Item de Pedido
├── repository/
│   ├── UserRepository.java
│   ├── CategoriaRepository.java
│   ├── ProductoRepository.java
│   └── PedidoRepository.java
├── security/
│   ├── JwtUtil.java                # Utilidades JWT
│   ├── JwtAuthenticationFilter.java # Filtro de autenticación
│   └── CustomUserDetailsService.java # Servicio de autenticación
└── service/
    ├── UserService.java
    ├── ProductoService.java
    ├── CategoriaService.java
    └── PedidoService.java
```

## Configuración (application.properties)

Las principales configuraciones están en `src/main/resources/application.properties`:

- Puerto del servidor: `8080`
- Base de datos: `pasteleria_db`
- Usuario MySQL: `root`
- Password MySQL: (vacío - XAMPP default)
- JWT Secret: Configurado en el archivo
- JWT Expiration: 24 horas (86400000 ms)

## Testing con Swagger

1. Accede a `http://localhost:8080/doc/swagger-ui.html`
2. Prueba el endpoint de login en **Autenticación** con:
   ```json
   {
     "email": "ana@duoc.cl",
     "password": "tu_password"
   }
   ```
3. Copia el token recibido
4. Haz clic en el botón **Authorize** en la parte superior
5. Ingresa: `Bearer <token_copiado>`
6. Ahora puedes probar todos los endpoints protegidos

## Problemas Comunes

**Error: Unknown database 'pasteleria_db'**

- Solución: Crea la base de datos en MySQL como se indica en "Configuración Inicial"

**Error: Access denied for user 'root'**

- Solución: Verifica las credenciales en `application.properties`

**Error: Unable to connect to MySQL**

- Solución: Asegúrate de que XAMPP esté ejecutándose y MySQL esté iniciado

## Contribución

1. Crea un branch para tu feature
2. Haz commit de tus cambios
3. Push al branch
4. Crea un Pull Request

## Licencia

Este proyecto es para fines educativos - DuocUC 2025
