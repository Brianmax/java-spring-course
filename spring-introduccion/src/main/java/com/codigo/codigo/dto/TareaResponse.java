package com.codigo.codigo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TareaResponse {
    private UUID id;
    private String titulo;
    private boolean completado;
    private String descripcion;
    private Date fechaFinalizacion;
    private UUID usuarioId;
}
