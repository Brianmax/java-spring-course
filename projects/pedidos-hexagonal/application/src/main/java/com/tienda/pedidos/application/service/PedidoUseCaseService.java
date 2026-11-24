package com.tienda.pedidos.application.service;

import com.tienda.pedidos.application.port.in.CancelarPedidoUseCase;
import com.tienda.pedidos.application.port.in.ConsultarPedidoUseCase;
import com.tienda.pedidos.application.port.in.CrearPedidoUseCase;
import com.tienda.pedidos.application.port.in.EntregarPedidoUseCase;
import com.tienda.pedidos.application.port.in.ExpirarPedidosVencidosUseCase;
import com.tienda.pedidos.application.port.out.NotificarEventoPedidoPort;
import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;
import com.tienda.pedidos.domain.exception.PedidoDuplicadoException;
import com.tienda.pedidos.domain.model.Pedido;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Servicio de aplicación en Java puro: coordina casos de uso mediante puertos sin importar Spring ni JPA.
 */
public class PedidoUseCaseService implements
        CrearPedidoUseCase,
        ConsultarPedidoUseCase,
        CancelarPedidoUseCase,
        EntregarPedidoUseCase,
        ExpirarPedidosVencidosUseCase {

    private final PedidoRepositoryPort pedidos;
    private final NotificarEventoPedidoPort notificador;
    private final ProcesarReembolsoPort reembolsos;
    private final Clock clock;

    public PedidoUseCaseService(
            PedidoRepositoryPort pedidos,
            NotificarEventoPedidoPort notificador,
            ProcesarReembolsoPort reembolsos,
            Clock clock
    ) {
        this.pedidos = Objects.requireNonNull(pedidos, "pedidos es obligatorio");
        this.notificador = Objects.requireNonNull(notificador, "notificador es obligatorio");
        this.reembolsos = Objects.requireNonNull(reembolsos, "reembolsos es obligatorio");
        this.clock = Objects.requireNonNull(clock, "clock es obligatorio");
    }

    @Override
    public Pedido crear(String id, int totalSoles) {
        Pedido nuevo = Pedido.crear(id, totalSoles, Instant.now(clock));
        if (pedidos.existePorId(nuevo.id())) {
            throw new PedidoDuplicadoException(nuevo.id());
        }
        return pedidos.guardar(nuevo);
    }

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return pedidos.buscarPorId(id);
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidos.listarTodos();
    }

    @Override
    public Optional<Pedido> cancelar(String id) {
        Optional<Pedido> encontrado = pedidos.buscarPorId(id);
        if (encontrado.isEmpty()) {
            return Optional.empty();
        }

        Pedido pedido = encontrado.get();
        pedido.cancelar();

        if (pedido.requiereReembolso()) {
            reembolsos.reembolsar(pedido.id(), pedido.totalSoles());
            pedido.marcarReembolsado();
        }

        Pedido guardado = pedidos.guardar(pedido);
        notificador.notificarCancelacion(guardado);
        return Optional.of(guardado);
    }

    @Override
    public Optional<Pedido> entregar(String id) {
        Optional<Pedido> encontrado = pedidos.buscarPorId(id);
        if (encontrado.isEmpty()) {
            return Optional.empty();
        }

        Pedido pedido = encontrado.get();
        pedido.entregar();
        return Optional.of(pedidos.guardar(pedido));
    }

    @Override
    public int expirarAnterioresA(Instant fechaLimite) {
        List<Pedido> vencidos = pedidos.buscarEnPreparacionAnterioresA(fechaLimite);
        for (Pedido pedido : vencidos) {
            pedido.expirar();
            Pedido guardado = pedidos.guardar(pedido);
            notificador.notificarExpiracion(guardado);
        }
        return vencidos.size();
    }
}
