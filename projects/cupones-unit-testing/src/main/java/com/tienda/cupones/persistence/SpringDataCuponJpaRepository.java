package com.tienda.cupones.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCuponJpaRepository extends JpaRepository<CuponEntity, String> {
}
