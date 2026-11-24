package com.tienda.cupones.service;

import com.tienda.cupones.domain.CalculadoraDescuento;
import com.tienda.cupones.domain.Cupon;
import com.tienda.cupones.domain.Pedido;
import com.tienda.cupones.domain.ResultadoDescuento;
import com.tienda.cupones.domain.exception.CuponInvalidoException;
import com.tienda.cupones.domain.exception.PedidoNoEncontradoException;
import com.tienda.cupones.port.CuponRepository;
import com.tienda.cupones.port.NotificadorCliente;
import com.tienda.cupones.port.PedidoRepository;

public class AplicarCuponService {

    private final PedidoRepository pedidoRepo;
    private final CuponRepository cuponRepo;
    private final NotificadorCliente notificador;
    private final CalculadoraDescuento calculadora;

    public AplicarCuponService(
            PedidoRepository pedidoRepo,
            CuponRepository cuponRepo,
            NotificadorCliente notificador,
            CalculadoraDescuento calculadora
    ) {
        this.pedidoRepo = pedidoRepo;
        this.cuponRepo = cuponRepo;
        this.notificador = notificador;
        this.calculadora = calculadora;
    }

    public ResultadoDescuento aplicar(String pedidoId, String codigoCupon) {
        Pedido pedido = pedidoRepo.buscarPorId(pedidoId)
                .orElseThrow(() -> new PedidoNoEncontradoException(pedidoId));

        Cupon cupon = cuponRepo.buscarPorCodigo(codigoCupon)
                .filter(Cupon::activo)
                .orElseThrow(() -> new CuponInvalidoException(codigoCupon));

        int descuento = calculadora.calcularDescuento(pedido.subtotal(), cupon);
        pedido.aplicarDescuento(cupon.codigo(), descuento);
        pedidoRepo.guardar(pedido);
        notificador.avisarDescuento(pedido.id(), descuento);

        return new ResultadoDescuento(
                pedido.id(),
                cupon.codigo(),
                pedido.subtotal(),
                descuento,
                pedido.totalFinal()
        );
    }
}
