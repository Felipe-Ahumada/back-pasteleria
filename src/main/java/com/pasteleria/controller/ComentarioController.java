package com.pasteleria.controller;

import com.pasteleria.model.Comentario;
import com.pasteleria.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comentarios")
@Tag(name = "Comentarios", description = "API de gestión de comentarios de blog")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    @Operation(summary = "Listar todos los comentarios", description = "Obtiene todos los comentarios")
    public List<Comentario> listarComentarios() {
        return comentarioService.getAllComentarios();
    }

    @GetMapping("/blog/{blogId}")
    @Operation(summary = "Obtener comentarios por blog", description = "Obtiene todos los comentarios de un blog específico")
    public List<Comentario> obtenerComentariosPorBlog(@PathVariable String blogId) {
        return comentarioService.getComentariosByBlogId(blogId);
    }

    @PostMapping
    @Operation(summary = "Crear comentario", description = "Crea un nuevo comentario")
    public ResponseEntity<Comentario> crearComentario(@RequestBody Comentario comentario) {
        try {
            Comentario nuevoComentario = comentarioService.createComentario(comentario);
            return ResponseEntity.ok(nuevoComentario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Eliminar comentario", description = "Elimina un comentario (requiere rol ADMIN o SUPERADMIN)")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        try {
            comentarioService.deleteComentario(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
