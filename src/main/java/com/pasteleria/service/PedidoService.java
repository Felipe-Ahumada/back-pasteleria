package com.pasteleria.service;

import com.pasteleria.model.Pedido;
import com.pasteleria.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPedidosPorUsuario(Long userId) {
        return pedidoRepository.findByUserId(userId);
    }

    public Pedido obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Autowired
    private com.pasteleria.repository.ProductoRepository productoRepository;

    public Pedido guardarPedido(Pedido pedido) {
        if (pedido.getItems() != null) {
            pedido.getItems().forEach(item -> {
                item.setPedido(pedido);

                // Deduct stock
                com.pasteleria.model.Producto producto = productoRepository.findById(item.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

                if (producto.getStock() < item.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
                }

                producto.setStock(producto.getStock() - item.getCantidad());
                productoRepository.save(producto);
            });
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarEstadoPedido(Long id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(estado.replace("\"", "").trim());
        return pedidoRepository.save(pedido);
    }
}
