package com.tienda.cupones.domain.exception;

public class PedidoNoEncontradoException extends RuntimeException {

    private final String pedidoId;

    public PedidoNoEncontradoException(String pedidoId) {
        super("No existe el pedido con id: " + pedidoId);
        this.pedidoId = pedidoId;
    }

    public String pedidoId() {
        return pedidoId;
    }
}
