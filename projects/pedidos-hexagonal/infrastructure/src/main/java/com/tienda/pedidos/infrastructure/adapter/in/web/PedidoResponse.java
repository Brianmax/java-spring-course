package com.tienda.pedidos.infrastructure.adapter.in.web;

import com.tienda.pedidos.domain.model.Pedido;

import java.time.Instant;

public record PedidoResponse(
        String id,
        int totalSoles,
        String estado,
        Instant creadoEn,
        boolean reembolsado
) {
    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(
                pedido.id(),
                pedido.totalSoles(),
                pedido.estado().name(),
                pedido.creadoEn(),
                pedido.reembolsado()
        );
    }
}
