package com.tienda.pedidos.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PedidosHexagonalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PedidosHexagonalApplication.class, args);
    }
}
