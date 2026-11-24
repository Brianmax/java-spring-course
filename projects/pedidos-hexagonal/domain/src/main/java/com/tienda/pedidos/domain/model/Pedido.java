package com.tienda.pedidos.domain.model;

import com.tienda.pedidos.domain.exception.PedidoEstadoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoNoCancelableException;

import java.time.Instant;
import java.util.Objects;

/**
 * Modelo de dominio puro: protege las reglas del pedido sin conocer Spring, JPA ni HTTP.
 */
public class Pedido {

    public static final int MONTO_MINIMO_REEMBOLSO_SOLES = 100;

    private final String id;
    private final int totalSoles;
    private final Instant creadoEn;
    private EstadoPedido estado;
    private boolean reembolsado;

    private Pedido(String id, int totalSoles, EstadoPedido estado, Instant creadoEn, boolean reembolsado) {
        if (id == null || id.isBlank()) {
            throw new PedidoInvalidoException("El identificador del pedido es obligatorio");
        }
        if (totalSoles <= 0) {
            throw new PedidoInvalidoException("El total del pedido debe ser mayor que cero");
        }
        if (estado == null) {
            throw new PedidoInvalidoException("El estado del pedido es obligatorio");
        }
        if (creadoEn == null) {
            throw new PedidoInvalidoException("La fecha de creación es obligatoria");
        }
        this.id = id.trim();
        this.totalSoles = totalSoles;
        this.estado = estado;
        this.creadoEn = creadoEn;
        this.reembolsado = reembolsado;
    }

    public static Pedido crear(String id, int totalSoles, Instant creadoEn) {
        return new Pedido(id, totalSoles, EstadoPedido.EN_PREPARACION, creadoEn, false);
    }

    public static Pedido reconstituir(
            String id,
            int totalSoles,
            EstadoPedido estado,
            Instant creadoEn,
            boolean reembolsado
    ) {
        return new Pedido(id, totalSoles, estado, creadoEn, reembolsado);
    }

    public void cancelar() {
        if (this.estado != EstadoPedido.EN_PREPARACION) {
            throw new PedidoNoCancelableException(this.id, this.estado);
        }
        this.estado = EstadoPedido.CANCELADO;
    }

    public void entregar() {
        if (this.estado != EstadoPedido.EN_PREPARACION) {
            throw new PedidoEstadoInvalidoException(this.id, this.estado, "entregar");
        }
        this.estado = EstadoPedido.ENTREGADO;
    }

    public void expirar() {
        if (this.estado != EstadoPedido.EN_PREPARACION) {
            throw new PedidoEstadoInvalidoException(this.id, this.estado, "expirar");
        }
        this.estado = EstadoPedido.EXPIRADO;
    }

    public boolean requiereReembolso() {
        return this.totalSoles >= MONTO_MINIMO_REEMBOLSO_SOLES;
    }

    public void marcarReembolsado() {
        if (this.estado != EstadoPedido.CANCELADO) {
            throw new PedidoEstadoInvalidoException(this.id, this.estado, "reembolsar");
        }
        this.reembolsado = true;
    }

    public String id() {
        return id;
    }

    public int totalSoles() {
        return totalSoles;
    }

    public EstadoPedido estado() {
        return estado;
    }

    public Instant creadoEn() {
        return creadoEn;
    }

    public boolean reembolsado() {
        return reembolsado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido other)) return false;
        return totalSoles == other.totalSoles
                && reembolsado == other.reembolsado
                && Objects.equals(id, other.id)
                && estado == other.estado
                && Objects.equals(creadoEn, other.creadoEn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalSoles, estado, creadoEn, reembolsado);
    }
}
