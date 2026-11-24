package com.tienda.cupones.domain.exception;

public class PedidoDuplicadoException extends RuntimeException {

    private final String pedidoId;

    public PedidoDuplicadoException(String pedidoId) {
        super("Ya existe un pedido con id: " + pedidoId);
        this.pedidoId = pedidoId;
    }

    public String pedidoId() {
        return pedidoId;
    }
}
