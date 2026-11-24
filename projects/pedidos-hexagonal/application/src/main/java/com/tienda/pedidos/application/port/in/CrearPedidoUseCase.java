package com.tienda.pedidos.application.port.in;

import com.tienda.pedidos.domain.model.Pedido;

public interface CrearPedidoUseCase {

    Pedido crear(String id, int totalSoles);
}
