package com.codigo.codigo.service;

import com.codigo.codigo.dto.TareaRequest;
import com.codigo.codigo.dto.TareaResponse;
import com.codigo.codigo.entity.TareaEntity;
import com.codigo.codigo.repository.TareaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;


@Service
public class TareaService {
    // depende de
    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public TareaResponse saveTarea(TareaRequest tareaRequest) {
        // el titulo no debe de contener caracteres especiales
        String titulo = tareaRequest.getTitulo();
        if(!titulo.matches("^[a-zA-Z0-9 ]+$")) {
            System.out.println("El titulo es incorrecto");
            return null;
        }
        // la fecha de finalizacion debe de ser mayor o igual que la fecha de creacion

        Date fechaFinalizacion = tareaRequest.getFechaFinalizacion();
        Date fechaCreacion = new Date(System.currentTimeMillis());
        if(fechaCreacion.after(fechaFinalizacion)) {
            System.out.println("Fecha incorrecta");
            return null;
        }
        TareaEntity tareaEntity = new TareaEntity();
        tareaEntity.setTitulo(tareaRequest.getTitulo());
        tareaEntity.setDescripcion(tareaRequest.getDescripcion());
        tareaEntity.setFechaFinalizacion(tareaRequest.getFechaFinalizacion());
        tareaRepository.save(tareaEntity);

        TareaResponse tareaResponse = new TareaResponse();
        tareaResponse.setId(tareaEntity.getId());
        tareaResponse.setTitulo(tareaEntity.getTitulo());
        tareaResponse.setDescripcion(tareaEntity.getDescripcion());
        tareaResponse.setFechaFinalizacion(tareaEntity.getFechaFinalizacion());
        tareaResponse.setCompletado(tareaEntity.isCompletado());
        return tareaResponse;
    }
}
