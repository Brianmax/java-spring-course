package com.tienda.pedidos.domain.exception;

public class PedidoInvalidoException extends RuntimeException {

    public PedidoInvalidoException(String message) {
        super(message);
    }
}
