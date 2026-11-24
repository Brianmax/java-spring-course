package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public Pedido toDomain(PedidoEntity entity) {
        return Pedido.reconstituir(
                entity.getId(),
                entity.getTotalSoles(),
                entity.getEstado(),
                entity.getCreadoEn(),
                entity.isReembolsado()
        );
    }

    public PedidoEntity toEntity(Pedido pedido) {
        return new PedidoEntity(
                pedido.id(),
                pedido.totalSoles(),
                pedido.estado(),
                pedido.creadoEn(),
                pedido.reembolsado()
        );
    }
}
