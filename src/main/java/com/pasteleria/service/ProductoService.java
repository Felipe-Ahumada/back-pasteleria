package com.pasteleria.service;

import com.pasteleria.model.Producto;
import com.pasteleria.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public List<Producto> obtenerProductosPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoDetails) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(productoDetails.getNombre());
        producto.setPrecio(productoDetails.getPrecio());
        producto.setDescripcion(productoDetails.getDescripcion());
        producto.setImagenPrincipal(productoDetails.getImagenPrincipal());
        producto.setImagenesDetalle(productoDetails.getImagenesDetalle());
        producto.setStock(productoDetails.getStock());
        producto.setStockCritico(productoDetails.getStockCritico());
        producto.setCategoria(productoDetails.getCategoria());

        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
