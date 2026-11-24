package com.tienda.pedidos.application.port.out;

public interface ProcesarReembolsoPort {

    void reembolsar(String pedidoId, int montoSoles);
}
