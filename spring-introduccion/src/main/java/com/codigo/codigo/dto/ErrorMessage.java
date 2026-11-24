package com.codigo.codigo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;
@Getter
@Setter
public class ErrorMessage {
    private Date timestamp = new Date(System.currentTimeMillis());
    private int estado;
    private String error;
    private String mensaje;
    private String path;
    private Map<String, String> errores;
}
