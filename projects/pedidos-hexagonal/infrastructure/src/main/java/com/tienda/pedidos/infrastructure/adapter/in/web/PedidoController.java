package com.tienda.pedidos.infrastructure.adapter.in.web;

import com.tienda.pedidos.application.port.in.CancelarPedidoUseCase;
import com.tienda.pedidos.application.port.in.ConsultarPedidoUseCase;
import com.tienda.pedidos.application.port.in.CrearPedidoUseCase;
import com.tienda.pedidos.application.port.in.EntregarPedidoUseCase;
import com.tienda.pedidos.domain.exception.PedidoInvalidoException;
import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final CrearPedidoUseCase crearPedido;
    private final ConsultarPedidoUseCase consultarPedido;
    private final CancelarPedidoUseCase cancelarPedido;
    private final EntregarPedidoUseCase entregarPedido;

    public PedidoController(
            CrearPedidoUseCase crearPedido,
            ConsultarPedidoUseCase consultarPedido,
            CancelarPedidoUseCase cancelarPedido,
            EntregarPedidoUseCase entregarPedido
    ) {
        this.crearPedido = crearPedido;
        this.consultarPedido = consultarPedido;
        this.cancelarPedido = cancelarPedido;
        this.entregarPedido = entregarPedido;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@RequestBody CrearPedidoRequest request) {
        if (request == null || request.totalSoles() == null) {
            throw new PedidoInvalidoException("Los campos id y totalSoles son obligatorios");
        }
        Pedido creado = crearPedido.crear(request.id(), request.totalSoles());
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoResponse.from(creado));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        List<PedidoResponse> respuesta = consultarPedido.listarTodos().stream()
                .map(PedidoResponse::from)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") String id) {
        return consultarPedido.buscarPorId(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(PedidoResponse.from(p)))
                .orElseGet(() -> notFound(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable("id") String id) {
        return cancelarPedido.cancelar(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(PedidoResponse.from(p)))
                .orElseGet(() -> notFound(id));
    }

    @PatchMapping("/{id}/entregar")
    public ResponseEntity<?> entregar(@PathVariable("id") String id) {
        return entregarPedido.entregar(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(PedidoResponse.from(p)))
                .orElseGet(() -> notFound(id));
    }

    private ResponseEntity<ErrorResponse> notFound(String id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "NOT_FOUND", "No se encontró el pedido " + id));
    }
}
