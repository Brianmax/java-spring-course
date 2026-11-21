package com.codigo.codigo.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.codigo.codigo.dto.TareaRequest;
import com.codigo.codigo.dto.TareaResponse;
import com.codigo.codigo.entity.TareaEntity;
import com.codigo.codigo.repository.TareaRepository;
import com.codigo.codigo.service.TareaService;
import lombok.Getter;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tarea")
public class TareController {

    // depende de
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
        return ResponseEntity.status(HttpStatus.CREATED).body(tr);
    }

    @PatchMapping("/completar-tarea/{id}")
    public ResponseEntity<TareaResponse> completarTarea(@PathVariable(name = "id") UUID idTarea) {
        TareaResponse tr = tareaService.completarTarea(idTarea);
        if(tr == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tr);
    }

    @GetMapping("/find")
    public ResponseEntity<List<TareaResponse>> findAll() {
        List<TareaResponse> trs = tareaService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(trs);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<TareaResponse> findById(@PathVariable UUID id) {
        return null;
    }

    @GetMapping("/find-tareas-usuario/{idUsuario}")
    public ResponseEntity<List<TareaResponse>> findTareasUsuario(@PathVariable UUID idUsuario) {
        List<TareaResponse> tr = tareaService.findTareasUsuario(idUsuario);
        if(tr == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tr);
    }

    @PatchMapping ("/cambiar-titulo/{id}")
    ResponseEntity<TareaResponse> cambiarTitulo(
            @RequestParam String nuevoTitulo,
            @PathVariable(name = "id") UUID idTarea
    ) {
        TareaResponse tr = tareaService.cambiarTitulo(nuevoTitulo, idTarea);
        if(tr == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tr);
    }
    // buscar una tarea por id
}
