package com.pasteleria.controller;

import com.pasteleria.service.BlogLikeService;
import com.pasteleria.security.JwtUtil;
import com.pasteleria.service.UserService;
import com.pasteleria.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blogs")
@Tag(name = "Blog Likes", description = "API para gestión de likes en blogs")
public class BlogLikeController {

    @Autowired
    private BlogLikeService blogLikeService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Toggle like", description = "Da o quita like a un blog (requiere autenticación)")
    public ResponseEntity<Boolean> toggleLike(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        UserResponse user = userService.getUserByCorreo(email);
        boolean isLiked = blogLikeService.toggleLike(id, user.getId());
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/{id}/likes/count")
    @Operation(summary = "Contar likes", description = "Obtiene el número total de likes de un blog")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long id) {
        return ResponseEntity.ok(blogLikeService.countLikes(id));
    }

    @GetMapping("/{id}/likes/status")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Estado de like", description = "Verifica si el usuario actual ha dado like al blog")
    public ResponseEntity<Boolean> getLikeStatus(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        UserResponse user = userService.getUserByCorreo(email);
        return ResponseEntity.ok(blogLikeService.hasLiked(id, user.getId()));
    }
}
