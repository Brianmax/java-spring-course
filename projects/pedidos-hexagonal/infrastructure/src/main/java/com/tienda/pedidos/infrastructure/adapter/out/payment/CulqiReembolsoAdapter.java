package com.tienda.pedidos.infrastructure.adapter.out.payment;

import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Adaptador alternativo simulado de pasarela Culqi (activar con el perfil "culqi").
 */
@Component
@Profile("culqi")
public class CulqiReembolsoAdapter implements ProcesarReembolsoPort {

    private static final Logger log = LoggerFactory.getLogger(CulqiReembolsoAdapter.class);

    @Override
    public void reembolsar(String pedidoId, int montoSoles) {
        log.info("[CULQI] Reembolso simulado aprobado para pedido {} por S/ {}", pedidoId, montoSoles);
    }
}
