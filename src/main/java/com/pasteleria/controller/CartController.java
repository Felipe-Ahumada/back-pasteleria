package com.pasteleria.controller;

import com.pasteleria.model.Cart;
import com.pasteleria.service.CartService;
import com.pasteleria.service.UserService;
import com.pasteleria.dto.UserResponse;
import com.pasteleria.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Carrito", description = "API de gestión del carrito de compras")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private Long getUserIdFromToken(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        UserResponse user = userService.getUserByCorreo(email);
        return user.getId();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener carrito", description = "Obtiene el carrito del usuario actual")
    public ResponseEntity<Cart> getCart(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/items")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Agregar item", description = "Agrega un producto al carrito")
    public ResponseEntity<Cart> addItem(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload) {
        Long userId = getUserIdFromToken(token);
        String productId = (String) payload.get("productId");
        int quantity = (int) payload.get("quantity");
        String message = (String) payload.get("message");
        return ResponseEntity.ok(cartService.addItemToCart(userId, productId, quantity, message));
    }

    @PutMapping("/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar cantidad", description = "Actualiza la cantidad de un item en el carrito")
    public ResponseEntity<Cart> updateItem(@RequestHeader("Authorization") String token, @PathVariable Long itemId, @RequestBody Map<String, Integer> payload) {
        Long userId = getUserIdFromToken(token);
        int quantity = payload.get("quantity");
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Eliminar item", description = "Elimina un item del carrito")
    public ResponseEntity<Cart> removeItem(@RequestHeader("Authorization") String token, @PathVariable Long itemId) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, itemId));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Vaciar carrito", description = "Elimina todos los items del carrito")
    public ResponseEntity<Void> clearCart(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
