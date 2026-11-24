package com.tienda.cupones.web.dto;

public record ErrorResponse(int status, String error, String message) {
}
