package com.tienda.pedidos.domain.exception;

public class PedidoDuplicadoException extends RuntimeException {

    private final String pedidoId;

    public PedidoDuplicadoException(String pedidoId) {
        super("Ya existe un pedido registrado con el identificador " + pedidoId);
        this.pedidoId = pedidoId;
    }

    public String pedidoId() {
        return pedidoId;
    }
}
