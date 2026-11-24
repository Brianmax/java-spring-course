package com.tienda.pedidos.domain.exception;

import com.tienda.pedidos.domain.model.EstadoPedido;

public class PedidoEstadoInvalidoException extends RuntimeException {

    private final String pedidoId;
    private final EstadoPedido estadoActual;

    public PedidoEstadoInvalidoException(String pedidoId, EstadoPedido estadoActual, String operacion) {
        super("No se puede " + operacion + " el pedido " + pedidoId + " en estado " + estadoActual);
        this.pedidoId = pedidoId;
        this.estadoActual = estadoActual;
    }

    public String pedidoId() {
        return pedidoId;
    }

    public EstadoPedido estadoActual() {
        return estadoActual;
    }
}
