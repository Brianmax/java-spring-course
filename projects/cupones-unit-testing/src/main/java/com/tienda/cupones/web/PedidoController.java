package com.tienda.cupones.web;

import com.tienda.cupones.domain.Pedido;
import com.tienda.cupones.domain.ResultadoDescuento;
import com.tienda.cupones.service.AplicarCuponService;
import com.tienda.cupones.service.PedidoService;
import com.tienda.cupones.web.dto.AplicarCuponRequest;
import com.tienda.cupones.web.dto.CrearPedidoRequest;
import com.tienda.cupones.web.dto.CuponResponse;
import com.tienda.cupones.web.dto.PedidoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PedidoController {

    private final PedidoService pedidoService;
    private final AplicarCuponService aplicarCuponService;

    public PedidoController(PedidoService pedidoService, AplicarCuponService aplicarCuponService) {
        this.pedidoService = pedidoService;
        this.aplicarCuponService = aplicarCuponService;
    }

    @GetMapping("/pedidos")
    public List<PedidoResponse> listarPedidos() {
        return pedidoService.listarPedidos().stream()
                .map(PedidoResponse::from)
                .toList();
    }

    @GetMapping("/pedidos/{id}")
    public PedidoResponse obtenerPedido(@PathVariable String id) {
        return PedidoResponse.from(pedidoService.obtenerPorId(id));
    }

    @PostMapping("/pedidos")
    public ResponseEntity<PedidoResponse> crearPedido(@RequestBody CrearPedidoRequest request) {
        Pedido creado = pedidoService.crearPedido(request.id(), request.subtotal());
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoResponse.from(creado));
    }

    @PostMapping("/pedidos/{id}/aplicar-cupon")
    public ResultadoDescuento aplicarCupon(
            @PathVariable String id,
            @RequestBody AplicarCuponRequest request
    ) {
        if (request == null || request.codigoCupon() == null || request.codigoCupon().isBlank()) {
            throw new IllegalArgumentException("El campo codigoCupon es obligatorio");
        }
        return aplicarCuponService.aplicar(id, request.codigoCupon());
    }

    @GetMapping("/cupones")
    public List<CuponResponse> listarCupones() {
        return pedidoService.listarCupones().stream()
                .map(CuponResponse::from)
                .toList();
    }
}
