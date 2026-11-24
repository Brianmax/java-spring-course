package com.tienda.pedidos.infrastructure.adapter.out.payment;

import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Adaptador simulado de pasarela Stripe (activo por defecto cuando no está el perfil "culqi").
 */
@Component
@Profile("!culqi")
public class StripeReembolsoAdapter implements ProcesarReembolsoPort {

    private static final Logger log = LoggerFactory.getLogger(StripeReembolsoAdapter.class);

    @Override
    public void reembolsar(String pedidoId, int montoSoles) {
        log.info("[STRIPE] Reembolso simulado aprobado para pedido {} por S/ {}", pedidoId, montoSoles);
    }
}
