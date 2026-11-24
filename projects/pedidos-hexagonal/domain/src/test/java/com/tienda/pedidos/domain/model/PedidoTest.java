package com.tienda.pedidos.domain.model;

import com.tienda.pedidos.domain.exception.PedidoEstadoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoNoCancelableException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private static final Instant FECHA_BASE = Instant.parse("2026-11-21T15:00:00Z");

    @Test
    void crearPedidoIniciaEnPreparacionYSinReembolso() {
        Pedido pedido = Pedido.crear("ORD-101", 120, FECHA_BASE);

        assertEquals("ORD-101", pedido.id());
        assertEquals(120, pedido.totalSoles());
        assertEquals(EstadoPedido.EN_PREPARACION, pedido.estado());
        assertFalse(pedido.reembolsado());
        assertTrue(pedido.requiereReembolso());
    }

    @Test
    void cancelarPedidoEnPreparacionCambiaEstadoACancelado() {
        Pedido pedido = Pedido.crear("ORD-101", 120, FECHA_BASE);

        pedido.cancelar();

        assertEquals(EstadoPedido.CANCELADO, pedido.estado());
    }

    @Test
    void cancelarPedidoEntregadoLanzaExcepcionYNoCambiaEstado() {
        Pedido pedido = Pedido.reconstituir("ORD-102", 85, EstadoPedido.ENTREGADO, FECHA_BASE, false);

        PedidoNoCancelableException ex = assertThrows(PedidoNoCancelableException.class, pedido::cancelar);

        assertEquals("ORD-102", ex.pedidoId());
        assertEquals(EstadoPedido.ENTREGADO, ex.estadoActual());
        assertEquals(EstadoPedido.ENTREGADO, pedido.estado());
    }

    @Test
    void pedidoMenorAlUmbralNoRequiereReembolso() {
        Pedido pedido = Pedido.crear("ORD-103", 50, FECHA_BASE);

        assertFalse(pedido.requiereReembolso());
    }

    @Test
    void entregarYExpirarSoloSePermitenEnPreparacion() {
        Pedido paraEntregar = Pedido.crear("ORD-104", 90, FECHA_BASE);
        paraEntregar.entregar();
        assertEquals(EstadoPedido.ENTREGADO, paraEntregar.estado());
        assertThrows(PedidoEstadoInvalidoException.class, paraEntregar::expirar);

        Pedido paraExpirar = Pedido.crear("ORD-105", 90, FECHA_BASE);
        paraExpirar.expirar();
        assertEquals(EstadoPedido.EXPIRADO, paraExpirar.estado());
        assertThrows(PedidoEstadoInvalidoException.class, paraExpirar::entregar);
    }

    @Test
    void rechazaDatosInvalidosAlCrear() {
        assertThrows(PedidoInvalidoException.class, () -> Pedido.crear("   ", 100, FECHA_BASE));
        assertThrows(PedidoInvalidoException.class, () -> Pedido.crear("ORD-106", 0, FECHA_BASE));
        assertThrows(PedidoInvalidoException.class, () -> Pedido.crear("ORD-106", -15, FECHA_BASE));
        assertThrows(PedidoInvalidoException.class, () -> Pedido.crear("ORD-106", 100, null));
    }
}
