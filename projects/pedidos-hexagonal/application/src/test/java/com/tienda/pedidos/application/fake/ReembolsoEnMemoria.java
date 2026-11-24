package com.tienda.pedidos.application.fake;

import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReembolsoEnMemoria implements ProcesarReembolsoPort {

    public record RegistroReembolso(String pedidoId, int montoSoles) {}

    private final List<RegistroReembolso> registros = new ArrayList<>();

    @Override
    public void reembolsar(String pedidoId, int montoSoles) {
        registros.add(new RegistroReembolso(pedidoId, montoSoles));
    }

    public List<RegistroReembolso> registros() {
        return Collections.unmodifiableList(registros);
    }
}
