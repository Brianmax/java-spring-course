package com.codigo.codigo.exception;

import com.codigo.codigo.dto.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Errores de validacion en @RequetsBody con @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
    ) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setTimestamp(new Date(System.currentTimeMillis()));
        errorResponse.setEstado(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Error en la validacion de los datos");

        Map<String, String> map = new HashMap<>();
        for(FieldError error: errors) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            map.put(field, message);
        }
        errorResponse.setErrores(map);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateResource(
            DuplicateResourceException ex
    ) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setEstado(HttpStatus.CONFLICT.value());
        errorMessage.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

}
