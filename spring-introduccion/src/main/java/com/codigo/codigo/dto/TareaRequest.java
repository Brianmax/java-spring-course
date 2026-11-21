package com.codigo.codigo.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class TareaRequest {
    private String titulo;
    private String descripcion;
    private Date fechaFinalizacion;
    private UUID usuarioId;
}
