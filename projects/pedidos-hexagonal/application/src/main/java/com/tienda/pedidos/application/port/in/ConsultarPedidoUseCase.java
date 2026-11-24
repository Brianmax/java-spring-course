package com.tienda.pedidos.application.port.in;

import com.tienda.pedidos.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface ConsultarPedidoUseCase {

    Optional<Pedido> buscarPorId(String id);

    List<Pedido> listarTodos();
}
