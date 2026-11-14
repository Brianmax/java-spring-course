package com.codigo.codigo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @GetMapping("/findAll")
    public String findAllProducts() {
        return "Todos los productos";
    }
}
