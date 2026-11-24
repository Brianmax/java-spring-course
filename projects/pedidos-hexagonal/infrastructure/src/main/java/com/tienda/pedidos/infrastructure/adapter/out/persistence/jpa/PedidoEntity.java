package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.domain.model.EstadoPedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Entidad JPA: representa una fila en la tabla SQL "pedidos". Vive solo en infraestructura.
 */
@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Column(name = "total_soles", nullable = false)
    private int totalSoles;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoPedido estado;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "reembolsado", nullable = false)
    private boolean reembolsado;

    public PedidoEntity() {
        // Requerido por Hibernate/JPA
    }

    public PedidoEntity(String id, int totalSoles, EstadoPedido estado, Instant creadoEn, boolean reembolsado) {
        this.id = id;
        this.totalSoles = totalSoles;
        this.estado = estado;
        this.creadoEn = creadoEn;
        this.reembolsado = reembolsado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalSoles() {
        return totalSoles;
    }

    public void setTotalSoles(int totalSoles) {
        this.totalSoles = totalSoles;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Instant creadoEn) {
        this.creadoEn = creadoEn;
    }

    public boolean isReembolsado() {
        return reembolsado;
    }

    public void setReembolsado(boolean reembolsado) {
        this.reembolsado = reembolsado;
    }
}
