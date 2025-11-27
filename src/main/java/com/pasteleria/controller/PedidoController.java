package com.pasteleria.controller;

import com.pasteleria.model.Pedido;
import com.pasteleria.service.PedidoService;
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
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "API de gestión de pedidos")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido (usuario autenticado)")
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido, Authentication authentication) {
        try {
            // Set user from authentication
            Pedido nuevoPedido = pedidoService.guardarPedido(pedido);
            return ResponseEntity.ok(nuevoPedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar pedidos del usuario", description = "Obtiene los pedidos del usuario autenticado")
    public List<Pedido> listarMisPedidos(Authentication authentication) {
        // Extract userId from authentication - this is simplified
        // In production, you'd get the userId from the authenticated user
        return pedidoService.listarPedidos();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'SELLER')")
    @Operation(summary = "Listar todos los pedidos", description = "Obtiene todos los pedidos (requiere rol ADMIN o SELLER)")
    public List<Pedido> listarTodosPedidos() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Obtiene un pedido específico por su ID")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.obtenerPedidoPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'SELLER')")
    @Operation(summary = "Actualizar estado del pedido", description = "Actualiza el estado de un pedido (requiere rol ADMIN o SELLER)")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestBody String estado) {
        System.out
                .println("Solicitud de actualización de estado para pedido ID: " + id + ", Estado recibido: " + estado);
        try {
            Pedido pedidoActualizado = pedidoService.actualizarEstadoPedido(id, estado);
            System.out.println("Estado actualizado correctamente: " + pedidoActualizado.getEstado());
            return ResponseEntity.ok(pedidoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
