package com.codigo.codigo.repository;

import com.codigo.codigo.entity.TareaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TareaRepository extends JpaRepository<TareaEntity, UUID> {
}
