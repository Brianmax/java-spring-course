package com.tienda.pedidos.application.service;

import com.tienda.pedidos.application.fake.NotificadorEnMemoria;
import com.tienda.pedidos.application.fake.ReembolsoEnMemoria;
import com.tienda.pedidos.application.fake.RepositorioPedidosEnMemoria;
import com.tienda.pedidos.domain.exception.PedidoDuplicadoException;
import com.tienda.pedidos.domain.exception.PedidoNoCancelableException;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PedidoUseCaseServiceTest {

    private static final Instant AHORA = Instant.parse("2026-11-21T18:00:00Z");

    private RepositorioPedidosEnMemoria repositorio;
    private NotificadorEnMemoria notificador;
    private ReembolsoEnMemoria reembolsos;
    private PedidoUseCaseService service;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioPedidosEnMemoria();
        notificador = new NotificadorEnMemoria();
        reembolsos = new ReembolsoEnMemoria();
        Clock relojFijo = Clock.fixed(AHORA, ZoneOffset.UTC);
        service = new PedidoUseCaseService(repositorio, notificador, reembolsos, relojFijo);
    }

    @Test
    void cancelarOrd101EnPreparacionConMontoAltoGuardaReembolsaYNotifica() {
        repositorio.guardar(Pedido.crear("ORD-101", 120, AHORA));

        Optional<Pedido> resultado = service.cancelar("ORD-101");

        assertTrue(resultado.isPresent());
        assertEquals(EstadoPedido.CANCELADO, resultado.get().estado());
        assertTrue(resultado.get().reembolsado());
        assertEquals(EstadoPedido.CANCELADO, repositorio.buscarPorId("ORD-101").orElseThrow().estado());
        assertEquals(List.of(new ReembolsoEnMemoria.RegistroReembolso("ORD-101", 120)), reembolsos.registros());
        assertEquals(List.of("ORD-101"), notificador.cancelaciones());
    }

    @Test
    void cancelarPedidoMenorAlUmbralNoInvocaPasarelaDeReembolsos() {
        repositorio.guardar(Pedido.crear("ORD-103", 50, AHORA));

        Optional<Pedido> resultado = service.cancelar("ORD-103");

        assertTrue(resultado.isPresent());
        assertEquals(EstadoPedido.CANCELADO, resultado.get().estado());
        assertFalse(resultado.get().reembolsado());
        assertTrue(reembolsos.registros().isEmpty());
        assertEquals(List.of("ORD-103"), notificador.cancelaciones());
    }

    @Test
    void cancelarOrd102EntregadoLanzaExcepcionSinGuardarNiReembolsarNiNotificar() {
        repositorio.guardar(Pedido.reconstituir("ORD-102", 85, EstadoPedido.ENTREGADO, AHORA, false));

        assertThrows(PedidoNoCancelableException.class, () -> service.cancelar("ORD-102"));

        assertEquals(EstadoPedido.ENTREGADO, repositorio.buscarPorId("ORD-102").orElseThrow().estado());
        assertTrue(reembolsos.registros().isEmpty());
        assertTrue(notificador.cancelaciones().isEmpty());
    }

    @Test
    void cancelarOrd999AusenteDevuelveVacioSinEfectosSecundarios() {
        Optional<Pedido> resultado = service.cancelar("ORD-999");

        assertTrue(resultado.isEmpty());
        assertTrue(reembolsos.registros().isEmpty());
        assertTrue(notificador.cancelaciones().isEmpty());
    }

    @Test
    void crearRechazaIdentificadorDuplicado() {
        service.crear("ORD-200", 150);

        assertThrows(PedidoDuplicadoException.class, () -> service.crear("ORD-200", 90));
    }

    @Test
    void entregarCambiaEstadoDeEnPreparacionAEntregado() {
        service.crear("ORD-201", 75);

        Optional<Pedido> entregado = service.entregar("ORD-201");

        assertTrue(entregado.isPresent());
        assertEquals(EstadoPedido.ENTREGADO, entregado.get().estado());
    }

    @Test
    void expirarPedidosVencidosSoloAfectaEnPreparacionAnterioresAlLimite() {
        Instant hace30Horas = AHORA.minusSeconds(30 * 3600);
        Instant hace2Horas = AHORA.minusSeconds(2 * 3600);
        repositorio.guardar(Pedido.crear("ORD-OLD", 110, hace30Horas));
        repositorio.guardar(Pedido.reconstituir("ORD-OLD-DONE", 80, EstadoPedido.ENTREGADO, hace30Horas, false));
        repositorio.guardar(Pedido.crear("ORD-RECENT", 90, hace2Horas));

        int expirados = service.expirarAnterioresA(AHORA.minusSeconds(24 * 3600));

        assertEquals(1, expirados);
        assertEquals(EstadoPedido.EXPIRADO, repositorio.buscarPorId("ORD-OLD").orElseThrow().estado());
        assertEquals(EstadoPedido.ENTREGADO, repositorio.buscarPorId("ORD-OLD-DONE").orElseThrow().estado());
        assertEquals(EstadoPedido.EN_PREPARACION, repositorio.buscarPorId("ORD-RECENT").orElseThrow().estado());
        assertEquals(List.of("ORD-OLD"), notificador.expiraciones());
    }
}
