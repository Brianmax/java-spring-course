package com.codigo.codigo.repository;

import com.codigo.codigo.entity.TareaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TareaRepository extends JpaRepository<TareaEntity, UUID> {

    @Query(nativeQuery = true, value = "select * from tarea where usuario_id=:idUsuario")
    List<TareaEntity> findByIdUsuario(UUID idUsuario);

}
