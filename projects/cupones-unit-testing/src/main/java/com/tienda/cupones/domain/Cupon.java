package com.tienda.cupones.domain;

public record Cupon(
        String codigo,
        int porcentaje,
        int minimo,
        int tope,
        boolean activo
) {
    public Cupon {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del cupón es obligatorio");
        }
        if (porcentaje <= 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 1 y 100");
        }
        if (minimo < 0 || tope < 0) {
            throw new IllegalArgumentException("El mínimo y el tope no pueden ser negativos");
        }
    }
}
