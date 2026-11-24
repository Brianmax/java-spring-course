package com.tienda.pedidos.application.fake;

import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositorioPedidosEnMemoria implements PedidoRepositoryPort {

    private final Map<String, Pedido> almacenamiento = new LinkedHashMap<>();

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return Optional.ofNullable(almacenamiento.get(id)).map(this::copiar);
    }

    @Override
    public List<Pedido> listarTodos() {
        return almacenamiento.values().stream().map(this::copiar).toList();
    }

    @Override
    public List<Pedido> buscarEnPreparacionAnterioresA(Instant fechaLimite) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : almacenamiento.values()) {
            if (pedido.estado() == EstadoPedido.EN_PREPARACION && pedido.creadoEn().isBefore(fechaLimite)) {
                resultado.add(copiar(pedido));
            }
        }
        return resultado;
    }

    @Override
    public boolean existePorId(String id) {
        return almacenamiento.containsKey(id);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        Pedido copia = copiar(pedido);
        almacenamiento.put(copia.id(), copia);
        return copiar(copia);
    }

    private Pedido copiar(Pedido original) {
        return Pedido.reconstituir(
                original.id(),
                original.totalSoles(),
                original.estado(),
                original.creadoEn(),
                original.reembolsado()
        );
    }
}
