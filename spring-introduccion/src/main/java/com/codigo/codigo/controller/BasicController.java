package com.codigo.codigo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// bean
public class BasicController {
    @GetMapping("/hola")
    public String hola() {
        System.out.println("Ingresamos al metodo hola...");
        return "Hola desde mi servidor de spring boot";
    }

    @GetMapping("/adios")
    public String adios() {
        System.out.println("Ingresamos al metodo adios...");
        return "Adios desde Spring Boot server";
    }
}
