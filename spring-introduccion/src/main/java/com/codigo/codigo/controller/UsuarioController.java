package com.codigo.codigo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @GetMapping("/findAll")
    public String findAllUsers() {
        return "Todos los usuarios";
    }
    @GetMapping("/delete")
    public String deleteUsers() {
        return "Eliminando usuarios";
    }
}
