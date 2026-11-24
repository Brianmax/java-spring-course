package com.tienda.cupones.domain.exception;

public class CuponInvalidoException extends RuntimeException {

    private final String codigoCupon;

    public CuponInvalidoException(String codigoCupon) {
        super("El cupón no existe o está inactivo: " + codigoCupon);
        this.codigoCupon = codigoCupon;
    }

    public String codigoCupon() {
        return codigoCupon;
    }
}
