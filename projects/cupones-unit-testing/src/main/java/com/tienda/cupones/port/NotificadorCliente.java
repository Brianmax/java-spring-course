package com.tienda.cupones.port;

public interface NotificadorCliente {

    void avisarDescuento(String pedidoId, int descuentoSoles);
}
