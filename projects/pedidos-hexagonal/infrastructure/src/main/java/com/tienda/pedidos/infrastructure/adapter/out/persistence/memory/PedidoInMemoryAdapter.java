package com.tienda.pedidos.infrastructure.adapter.out.persistence.memory;

import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador de persistencia en memoria para arrancar y demostrar la API sin PostgreSQL ni Docker.
 */
@Repository
@Profile("inmemory")
public class PedidoInMemoryAdapter implements PedidoRepositoryPort {

    private final Map<String, Pedido> almacenamiento = new ConcurrentHashMap<>();

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return Optional.ofNullable(almacenamiento.get(id)).map(this::copiar);
    }

    @Override
    public List<Pedido> listarTodos() {
        return almacenamiento.values().stream()
                .sorted(Comparator.comparing(Pedido::id))
                .map(this::copiar)
                .toList();
    }

    @Override
    public List<Pedido> buscarEnPreparacionAnterioresA(Instant fechaLimite) {
        return almacenamiento.values().stream()
                .filter(p -> p.estado() == EstadoPedido.EN_PREPARACION && p.creadoEn().isBefore(fechaLimite))
                .map(this::copiar)
                .toList();
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
