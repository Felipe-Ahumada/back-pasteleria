package com.pasteleria.controller;

import com.pasteleria.model.Blog;
import com.pasteleria.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
@Tag(name = "Blogs", description = "API para gestión de blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    @Operation(summary = "Obtener todos los blogs", description = "Retorna la lista de todos los blogs")
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener blog por ID", description = "Retorna un blog específico por su ID")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        return blogService.getBlogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Crear nuevo blog", description = "Crea un nuevo blog (requiere autenticación)")
    public Blog createBlog(@RequestBody Blog blog) {
        return blogService.createBlog(blog);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @Operation(summary = "Actualizar estado del blog", description = "Actualiza el estado de un blog (requiere rol ADMIN o SUPERADMIN)")
    public ResponseEntity<Blog> updateStatus(@PathVariable Long id, @RequestBody String status) {
        // Sanitize status string (remove quotes if present)
        String sanitizedStatus = status.replace("\"", "").trim();
        try {
            return ResponseEntity.ok(blogService.updateStatus(id, sanitizedStatus));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @Operation(summary = "Eliminar blog", description = "Elimina un blog por su ID (requiere rol ADMIN o SUPERADMIN)")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }
}
