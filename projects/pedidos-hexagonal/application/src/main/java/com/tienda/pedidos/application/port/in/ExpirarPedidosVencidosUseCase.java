package com.tienda.pedidos.application.port.in;

import java.time.Instant;

public interface ExpirarPedidosVencidosUseCase {

    int expirarAnterioresA(Instant fechaLimite);
}
