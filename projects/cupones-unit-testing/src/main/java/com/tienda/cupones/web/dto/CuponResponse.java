package com.tienda.cupones.web.dto;

import com.tienda.cupones.domain.Cupon;

public record CuponResponse(
        String codigo,
        int porcentaje,
        int minimo,
        int tope,
        boolean activo
) {
    public static CuponResponse from(Cupon cupon) {
        return new CuponResponse(
                cupon.codigo(),
                cupon.porcentaje(),
                cupon.minimo(),
                cupon.tope(),
                cupon.activo()
        );
    }
}
