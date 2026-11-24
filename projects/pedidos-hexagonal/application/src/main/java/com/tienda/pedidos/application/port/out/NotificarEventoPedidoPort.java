package com.tienda.pedidos.application.port.out;

import com.tienda.pedidos.domain.model.Pedido;

public interface NotificarEventoPedidoPort {

    void notificarCancelacion(Pedido pedido);

    void notificarExpiracion(Pedido pedido);
}
