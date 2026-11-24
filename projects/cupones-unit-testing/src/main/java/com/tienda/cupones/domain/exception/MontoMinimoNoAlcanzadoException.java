package com.tienda.cupones.domain.exception;

public class MontoMinimoNoAlcanzadoException extends RuntimeException {

    private final int minimoRequerido;
    private final int subtotalRecibido;

    public MontoMinimoNoAlcanzadoException(int minimoRequerido, int subtotalRecibido) {
        super("El subtotal S/ " + subtotalRecibido + " no alcanza el monto mínimo de S/ " + minimoRequerido);
        this.minimoRequerido = minimoRequerido;
        this.subtotalRecibido = subtotalRecibido;
    }

    public int minimoRequerido() {
        return minimoRequerido;
    }

    public int subtotalRecibido() {
        return subtotalRecibido;
    }
}
