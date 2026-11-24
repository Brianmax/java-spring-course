package com.tienda.pedidos.application.fake;

import com.tienda.pedidos.application.port.out.NotificarEventoPedidoPort;
import com.tienda.pedidos.domain.model.Pedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificadorEnMemoria implements NotificarEventoPedidoPort {

    private final List<String> cancelaciones = new ArrayList<>();
    private final List<String> expiraciones = new ArrayList<>();

    @Override
    public void notificarCancelacion(Pedido pedido) {
        cancelaciones.add(pedido.id());
    }

    @Override
    public void notificarExpiracion(Pedido pedido) {
        expiraciones.add(pedido.id());
    }

    public List<String> cancelaciones() {
        return Collections.unmodifiableList(cancelaciones);
    }

    public List<String> expiraciones() {
        return Collections.unmodifiableList(expiraciones);
    }
}
