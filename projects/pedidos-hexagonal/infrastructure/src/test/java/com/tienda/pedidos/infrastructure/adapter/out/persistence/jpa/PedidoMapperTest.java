package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PedidoMapperTest {

    private final PedidoMapper mapper = new PedidoMapper();

    @Test
    void traduceIdaYVueltaEntreDominioYEntidadJpa() {
        Instant fecha = Instant.parse("2026-11-21T16:30:00Z");
        Pedido original = Pedido.reconstituir("ORD-101", 120, EstadoPedido.CANCELADO, fecha, true);

        PedidoEntity entity = mapper.toEntity(original);
        Pedido reconstruido = mapper.toDomain(entity);

        assertEquals("ORD-101", entity.getId());
        assertEquals(120, entity.getTotalSoles());
        assertEquals(EstadoPedido.CANCELADO, entity.getEstado());
        assertEquals(fecha, entity.getCreadoEn());
        assertTrue(entity.isReembolsado());
        assertEquals(original, reconstruido);
    }
}
