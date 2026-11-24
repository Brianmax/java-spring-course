package com.tienda.pedidos.application.port.in;

import com.tienda.pedidos.domain.model.Pedido;

import java.util.Optional;

public interface CancelarPedidoUseCase {

    Optional<Pedido> cancelar(String id);
}
