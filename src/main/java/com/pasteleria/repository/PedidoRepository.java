package com.pasteleria.repository;

import com.pasteleria.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUserId(Long userId);

    List<Pedido> findByEstado(String estado);
}
