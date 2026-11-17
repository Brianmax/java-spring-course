package com.codigo.codigo.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class TareaResponse {
    private UUID id;
    private String titulo;
    private boolean completado;
    private String descripcion;
    private Date fechaFinalizacion;
}
