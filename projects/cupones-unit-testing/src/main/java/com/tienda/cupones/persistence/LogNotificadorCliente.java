package com.tienda.cupones.persistence;

import com.tienda.cupones.port.NotificadorCliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogNotificadorCliente implements NotificadorCliente {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogNotificadorCliente.class);

    @Override
    public void avisarDescuento(String pedidoId, int descuentoSoles) {
        LOGGER.info("[NOTIFICACION] Pedido {} recibió un descuento de S/ {}", pedidoId, descuentoSoles);
    }
}
