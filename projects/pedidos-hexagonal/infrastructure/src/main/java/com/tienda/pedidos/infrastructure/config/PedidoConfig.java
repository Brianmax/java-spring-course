package com.tienda.pedidos.infrastructure.config;

import com.tienda.pedidos.application.port.out.NotificarEventoPedidoPort;
import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;
import com.tienda.pedidos.application.service.PedidoUseCaseService;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.Instant;

/**
 * Ensamblaje Spring Boot: conecta el núcleo Java puro con los adaptadores activos según el perfil.
 */
@Configuration
public class PedidoConfig {

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    public PedidoUseCaseService pedidoUseCaseService(
            PedidoRepositoryPort pedidoRepositoryPort,
            NotificarEventoPedidoPort notificarEventoPedidoPort,
            ProcesarReembolsoPort procesarReembolsoPort,
            Clock systemClock
    ) {
        return new PedidoUseCaseService(
                pedidoRepositoryPort,
                notificarEventoPedidoPort,
                procesarReembolsoPort,
                systemClock
        );
    }

    @Bean
    @Profile("inmemory")
    public CommandLineRunner seedDemoPedidos(PedidoRepositoryPort pedidos, Clock clock) {
        return args -> {
            Instant ahora = Instant.now(clock);
            if (!pedidos.existePorId("ORD-101")) {
                pedidos.guardar(Pedido.crear("ORD-101", 120, ahora));
            }
            if (!pedidos.existePorId("ORD-102")) {
                pedidos.guardar(Pedido.reconstituir("ORD-102", 85, EstadoPedido.ENTREGADO, ahora, false));
            }
        };
    }
}
