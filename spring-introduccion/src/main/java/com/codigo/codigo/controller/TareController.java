package com.codigo.codigo.controller;

import com.codigo.codigo.entity.TareaEntity;
import com.codigo.codigo.repository.TareaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tarea")
public class TareController {
    private final TareaRepository tareaRepository;

    public TareController(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }


    @PostMapping("/save")
    public TareaEntity saveTarea(@RequestBody TareaEntity tareaEntity) {
        System.out.println(tareaEntity);
        tareaRepository.save(tareaEntity);
        return tareaEntity;
    }
}
