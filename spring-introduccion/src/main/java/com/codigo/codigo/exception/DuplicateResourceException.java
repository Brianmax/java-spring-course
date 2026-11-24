package com.codigo.codigo.exception;

import org.springframework.validation.FieldError;

import java.util.ArrayList;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

}
