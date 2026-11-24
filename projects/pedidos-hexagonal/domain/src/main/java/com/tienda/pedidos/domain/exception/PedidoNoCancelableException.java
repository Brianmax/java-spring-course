package com.tienda.pedidos.domain.exception;

import com.tienda.pedidos.domain.model.EstadoPedido;

public class PedidoNoCancelableException extends RuntimeException {

    private final String pedidoId;
    private final EstadoPedido estadoActual;

    public PedidoNoCancelableException(String pedidoId, EstadoPedido estadoActual) {
        super("El pedido " + pedidoId + " no puede cancelarse porque está en estado " + estadoActual);
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
