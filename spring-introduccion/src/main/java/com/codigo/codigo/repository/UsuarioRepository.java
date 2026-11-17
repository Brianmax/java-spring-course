package com.codigo.codigo.repository;

import com.codigo.codigo.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {

    // Spring Data JPA crea la consulta a partir del nombre del método.
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
