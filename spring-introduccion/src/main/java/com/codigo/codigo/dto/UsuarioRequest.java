package com.codigo.codigo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private Integer edad;
    private String password;
}
