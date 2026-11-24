package com.tienda.pedidos.infrastructure.adapter.in.web;

import com.tienda.pedidos.domain.exception.PedidoDuplicadoException;
import com.tienda.pedidos.domain.exception.PedidoEstadoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoNoCancelableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PedidoExceptionHandler {

    @ExceptionHandler(PedidoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleInvalido(PedidoInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "BAD_REQUEST", ex.getMessage()));
    }

    @ExceptionHandler({
            PedidoNoCancelableException.class,
            PedidoEstadoInvalidoException.class,
            PedidoDuplicadoException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, "CONFLICT", ex.getMessage()));
    }
}
