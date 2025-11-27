package com.pasteleria.controller;

import com.pasteleria.dto.UserResponse;
import com.pasteleria.model.User;
import com.pasteleria.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios", description = "API de gestión de usuarios")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios (requiere rol ADMIN)")
    public List<UserResponse> listarUsuarios() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN') or @userService.getUserById(#id).correo == authentication.name")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario por su ID (ADMIN o el mismo usuario)")
    public ResponseEntity<UserResponse> obtenerUsuario(@PathVariable Long id, Authentication authentication) {
        try {
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN') or @userService.getUserById(#id).correo == authentication.name")
    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario (ADMIN o el mismo usuario)")
    public ResponseEntity<User> actualizarUsuario(@PathVariable Long id, @RequestBody User user,
            Authentication authentication) {
        try {
            User usuarioActualizado = userService.updateUser(id, user);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario (requiere rol ADMIN)")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
