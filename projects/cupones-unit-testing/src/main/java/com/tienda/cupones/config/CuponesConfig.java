package com.tienda.cupones.config;

import com.tienda.cupones.domain.CalculadoraDescuento;
import com.tienda.cupones.domain.Cupon;
import com.tienda.cupones.domain.Pedido;
import com.tienda.cupones.port.CuponRepository;
import com.tienda.cupones.port.NotificadorCliente;
import com.tienda.cupones.port.PedidoRepository;
import com.tienda.cupones.service.AplicarCuponService;
import com.tienda.cupones.service.PedidoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuponesConfig {

    @Bean
    public CalculadoraDescuento calculadoraDescuento() {
        return new CalculadoraDescuento();
    }

    @Bean
    public AplicarCuponService aplicarCuponService(
            PedidoRepository pedidoRepo,
            CuponRepository cuponRepo,
            NotificadorCliente notificador,
            CalculadoraDescuento calculadora
    ) {
        return new AplicarCuponService(pedidoRepo, cuponRepo, notificador, calculadora);
    }

    @Bean
    public PedidoService pedidoService(PedidoRepository pedidoRepo, CuponRepository cuponRepo) {
        return new PedidoService(pedidoRepo, cuponRepo);
    }

    @Bean
    public CommandLineRunner cargarDatosDemo(PedidoRepository pedidoRepo, CuponRepository cuponRepo) {
        return args -> {
            if (cuponRepo.buscarPorCodigo("DESC20").isEmpty()) {
                cuponRepo.guardar(new Cupon("DESC20", 20, 100, 50, true));
            }
            if (cuponRepo.buscarPorCodigo("VENCIDO10").isEmpty()) {
                cuponRepo.guardar(new Cupon("VENCIDO10", 10, 100, 50, false));
            }
            cargarPedidoSiNoExiste(pedidoRepo, "ORD-205", 200);
            cargarPedidoSiNoExiste(pedidoRepo, "ORD-206", 400);
            cargarPedidoSiNoExiste(pedidoRepo, "ORD-207", 100);
            cargarPedidoSiNoExiste(pedidoRepo, "ORD-208", 80);
        };
    }

    private void cargarPedidoSiNoExiste(PedidoRepository pedidoRepo, String id, int subtotal) {
        if (!pedidoRepo.existePorId(id)) {
            pedidoRepo.guardar(new Pedido(id, subtotal));
        }
    }
}
