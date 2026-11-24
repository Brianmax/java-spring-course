package com.tienda.cupones.domain;

import com.tienda.cupones.domain.exception.CuponInvalidoException;
import com.tienda.cupones.domain.exception.MontoMinimoNoAlcanzadoException;

/**
 * Regla de negocio de TICKET-14:
 * - El cupón debe estar activo.
 * - El subtotal debe cumplir el monto mínimo (desde S/ 100 inclusive para DESC20).
 * - Se aplica el porcentaje configurado sin superar el tope máximo del cupón.
 */
public class CalculadoraDescuento {

    public int calcularDescuento(int subtotal, Cupon cupon) {
        if (subtotal <= 0) {
            throw new IllegalArgumentException("El subtotal debe ser mayor a cero");
        }
        if (cupon == null || !cupon.activo()) {
            throw new CuponInvalidoException(cupon == null ? "null" : cupon.codigo());
        }
        if (cumpleMontoMinimo(subtotal, cupon.minimo())) {
            int bruto = subtotal * cupon.porcentaje() / 100;
            return Math.min(bruto, cupon.tope());
        }
        throw new MontoMinimoNoAlcanzadoException(cupon.minimo(), subtotal);
    }

    public boolean cumpleMontoMinimo(int subtotal, int minimo) {
        return subtotal >= minimo;
    }
}
