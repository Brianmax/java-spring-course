package com.tienda.cupones.service;

import com.tienda.cupones.domain.Cupon;
import com.tienda.cupones.domain.Pedido;
import com.tienda.cupones.domain.exception.PedidoDuplicadoException;
import com.tienda.cupones.domain.exception.PedidoNoEncontradoException;
import com.tienda.cupones.port.CuponRepository;
import com.tienda.cupones.port.PedidoRepository;

import java.util.List;

public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final CuponRepository cuponRepo;

    public PedidoService(PedidoRepository pedidoRepo, CuponRepository cuponRepo) {
        this.pedidoRepo = pedidoRepo;
        this.cuponRepo = cuponRepo;
    }

    public Pedido crearPedido(String id, int subtotal) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El identificador del pedido es obligatorio");
        }
        if (pedidoRepo.existePorId(id)) {
            throw new PedidoDuplicadoException(id);
        }
        return pedidoRepo.guardar(new Pedido(id, subtotal));
    }

    public Pedido obtenerPorId(String id) {
        return pedidoRepo.buscarPorId(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepo.listarTodos();
    }

    public List<Cupon> listarCupones() {
        return cuponRepo.listarTodos();
    }
}
