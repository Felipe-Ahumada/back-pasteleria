package com.pasteleria.controller;

import com.pasteleria.model.Producto;
import com.pasteleria.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API de gestión de productos")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos disponibles")
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto específico por su ID")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerProductoPorId(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Obtener productos por categoría", description = "Obtiene todos los productos de una categoría específica")
    public List<Producto> obtenerProductosPorCategoria(@PathVariable Long categoriaId) {
        return productoService.obtenerProductosPorCategoria(categoriaId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'SELLER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto (requiere rol ADMIN o SELLER)")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.guardarProducto(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'SELLER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente (requiere rol ADMIN o SELLER)")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto (requiere rol ADMIN)")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
