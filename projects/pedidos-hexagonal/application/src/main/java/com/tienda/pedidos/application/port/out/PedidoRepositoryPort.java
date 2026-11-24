package com.tienda.pedidos.application.port.out;

import com.tienda.pedidos.domain.model.Pedido;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PedidoRepositoryPort {

    Optional<Pedido> buscarPorId(String id);

    List<Pedido> listarTodos();

    List<Pedido> buscarEnPreparacionAnterioresA(Instant fechaLimite);

    boolean existePorId(String id);

    Pedido guardar(Pedido pedido);
}
