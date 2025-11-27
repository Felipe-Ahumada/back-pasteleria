package com.pasteleria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_items")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference
    private Pedido pedido;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonProperty("producto")
    private Producto producto;

    @Column(nullable = false)
    private String productoNombre;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double subtotal;

    private String mensaje;
}
