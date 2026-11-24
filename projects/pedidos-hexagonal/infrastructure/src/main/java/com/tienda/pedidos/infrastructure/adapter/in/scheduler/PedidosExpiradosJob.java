package com.tienda.pedidos.infrastructure.adapter.in.scheduler;

import com.tienda.pedidos.application.port.in.ExpirarPedidosVencidosUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/**
 * Segundo adaptador de entrada: reutiliza el núcleo desde una tarea programada sin pasar por HTTP.
 */
@Component
public class PedidosExpiradosJob {

    private static final Logger log = LoggerFactory.getLogger(PedidosExpiradosJob.class);

    private final ExpirarPedidosVencidosUseCase expirarPedidos;
    private final Clock clock;
    private final long horasExpiracion;

    public PedidosExpiradosJob(
            ExpirarPedidosVencidosUseCase expirarPedidos,
            Clock clock,
            @Value("${pedidos.expiracion.horas:24}") long horasExpiracion
    ) {
        this.expirarPedidos = expirarPedidos;
        this.clock = clock;
        this.horasExpiracion = horasExpiracion;
    }

    @Scheduled(cron = "${pedidos.scheduler.cron:0 */30 * * * *}")
    public void ejecutar() {
        Instant limite = Instant.now(clock).minus(Duration.ofHours(horasExpiracion));
        int expirados = expirarPedidos.expirarAnterioresA(limite);
        if (expirados > 0) {
            log.info("[SCHEDULER] Se expiraron {} pedido(s) anteriores a {}", expirados, limite);
        }
    }
}
