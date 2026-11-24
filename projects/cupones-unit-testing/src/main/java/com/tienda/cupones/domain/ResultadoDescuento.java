package com.tienda.cupones.domain;

public record ResultadoDescuento(
        String pedidoId,
        String codigoCupon,
        int subtotal,
        int descuento,
        int totalFinal
) {
}
