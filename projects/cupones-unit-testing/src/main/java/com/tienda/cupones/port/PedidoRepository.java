package com.tienda.cupones.port;

import com.tienda.cupones.domain.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository {

    Optional<Pedido> buscarPorId(String id);

    Pedido guardar(Pedido pedido);

    List<Pedido> listarTodos();

    boolean existePorId(String id);
}
