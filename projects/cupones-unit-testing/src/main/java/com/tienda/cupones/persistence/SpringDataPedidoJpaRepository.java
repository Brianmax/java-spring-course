package com.tienda.cupones.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPedidoJpaRepository extends JpaRepository<PedidoEntity, String> {
}
