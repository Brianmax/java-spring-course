package com.tienda.cupones.domain;

public class Pedido {

    private final String id;
    private final int subtotal;
    private int descuentoAplicado;
    private int totalFinal;
    private String codigoCuponAplicado;
    private boolean envioGratis;

    public Pedido(String id, int subtotal) {
        this(id, subtotal, 0, subtotal, null, false);
    }

    public Pedido(
            String id,
            int subtotal,
            int descuentoAplicado,
            int totalFinal,
            String codigoCuponAplicado,
            boolean envioGratis
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El identificador del pedido es obligatorio");
        }
        if (subtotal <= 0) {
            throw new IllegalArgumentException("El subtotal del pedido debe ser mayor a cero");
        }
        this.id = id;
        this.subtotal = subtotal;
        this.descuentoAplicado = descuentoAplicado;
        this.totalFinal = totalFinal;
        this.codigoCuponAplicado = codigoCuponAplicado;
        this.envioGratis = envioGratis;
    }

    public void aplicarDescuento(String codigoCupon, int descuento) {
        if (descuento < 0 || descuento > subtotal) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y el subtotal");
        }
        this.codigoCuponAplicado = codigoCupon;
        this.descuentoAplicado = descuento;
        this.totalFinal = subtotal - descuento;
    }

    public void marcarEnvioGratis(boolean envioGratis) {
        this.envioGratis = envioGratis;
    }

    public String id() {
        return id;
    }

    public int subtotal() {
        return subtotal;
    }

    public int descuentoAplicado() {
        return descuentoAplicado;
    }

    public int totalFinal() {
        return totalFinal;
    }

    public String codigoCuponAplicado() {
        return codigoCuponAplicado;
    }

    public boolean envioGratis() {
        return envioGratis;
    }
}
