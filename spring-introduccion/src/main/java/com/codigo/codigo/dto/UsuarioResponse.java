package com.codigo.codigo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsuarioResponse {
    private UUID id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private Integer edad;
}
