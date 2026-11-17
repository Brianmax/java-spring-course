package com.codigo.codigo.controller;

import com.codigo.codigo.dto.TareaRequest;
import com.codigo.codigo.dto.TareaResponse;
import com.codigo.codigo.entity.TareaEntity;
import com.codigo.codigo.repository.TareaRepository;
import com.codigo.codigo.service.TareaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tarea")
public class TareController {

    private final TareaService tareaService;
    public TareController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PostMapping("/save")
    public ResponseEntity<TareaResponse> saveTarea(@RequestBody TareaRequest tareaRequest) {
        TareaResponse tr = tareaService.saveTarea(tareaRequest);
        if(tr == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return null;
    }

    // Usuario
    // id UUID
    // username
    // email
    // nombre
    // apellido
    // edad (opcional)
    // password

    // insertar un usuario usando la api
}
