package com.tienda.pedidos.infrastructure.adapter.in.web;

public record ErrorResponse(int status, String error, String message) {
}
