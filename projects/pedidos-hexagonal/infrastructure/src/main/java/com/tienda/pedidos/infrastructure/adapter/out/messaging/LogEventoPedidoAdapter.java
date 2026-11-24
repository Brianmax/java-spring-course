package com.tienda.pedidos.infrastructure.adapter.out.messaging;

import com.tienda.pedidos.application.port.out.NotificarEventoPedidoPort;
import com.tienda.pedidos.domain.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adaptador de mensajería simulado por log. Puede reemplazarse por un adaptador Kafka sin tocar el núcleo.
 */
@Component
public class LogEventoPedidoAdapter implements NotificarEventoPedidoPort {

    private static final Logger log = LoggerFactory.getLogger(LogEventoPedidoAdapter.class);

    @Override
    public void notificarCancelacion(Pedido pedido) {
        log.info(
                "[EVENTO · topic=pedidos-cancelados] {\"id\":\"{}\",\"totalSoles\":{},\"estado\":\"{}\",\"reembolsado\":{}}",
                pedido.id(), pedido.totalSoles(), pedido.estado(), pedido.reembolsado()
        );
    }

    @Override
    public void notificarExpiracion(Pedido pedido) {
        log.info(
                "[EVENTO · topic=pedidos-expirados] {\"id\":\"{}\",\"totalSoles\":{},\"estado\":\"{}\"}",
                pedido.id(), pedido.totalSoles(), pedido.estado()
        );
    }
}
