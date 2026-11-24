package com.tienda.cupones.web.dto;

import com.tienda.cupones.domain.Pedido;

public record PedidoResponse(
        String id,
        int subtotal,
        int descuentoAplicado,
        int totalFinal,
        String codigoCuponAplicado,
        boolean envioGratis
) {
    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(
                pedido.id(),
                pedido.subtotal(),
                pedido.descuentoAplicado(),
                pedido.totalFinal(),
                pedido.codigoCuponAplicado(),
                pedido.envioGratis()
        );
    }
}
