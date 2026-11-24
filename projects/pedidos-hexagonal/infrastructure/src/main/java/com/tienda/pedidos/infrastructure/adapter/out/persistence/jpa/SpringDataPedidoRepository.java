package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.domain.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SpringDataPedidoRepository extends JpaRepository<PedidoEntity, String> {

    List<PedidoEntity> findByEstadoAndCreadoEnBefore(EstadoPedido estado, Instant fechaLimite);
}
