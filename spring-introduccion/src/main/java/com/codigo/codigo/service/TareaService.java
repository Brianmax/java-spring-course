package com.codigo.codigo.service;

import com.codigo.codigo.dto.TareaRequest;
import com.codigo.codigo.dto.TareaResponse;
import com.codigo.codigo.entity.TareaEntity;
import com.codigo.codigo.entity.UsuarioEntity;
import com.codigo.codigo.repository.TareaRepository;
import com.codigo.codigo.repository.UsuarioRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class TareaService {
    // depende de
    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    public TareaService(TareaRepository tareaRepository, UsuarioRepository usuarioRepository) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
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

        // verificar el usuario
        UUID usuarioId = tareaRequest.getUsuarioId();
        UsuarioEntity usuarioEntity = usuarioRepository.findById(usuarioId).orElse(null);
        if(usuarioEntity == null) {
            System.out.println("Usuario no existe");
            return null;
        }

        if(usuarioEntity.getTareasActivas() >= 10) {
            System.out.println("El usuario no puede tener mas de diez tareas");
            return null;
        }

        TareaEntity tareaEntity = new TareaEntity();
        tareaEntity.setTitulo(tareaRequest.getTitulo());
        tareaEntity.setDescripcion(tareaRequest.getDescripcion());
        tareaEntity.setFechaFinalizacion(tareaRequest.getFechaFinalizacion());
        tareaEntity.setUsuarioEntity(usuarioEntity);

        int tareasActivas = usuarioEntity.getTareasActivas();
        usuarioEntity.setTareasActivas(tareasActivas + 1);
        tareaRepository.save(tareaEntity);
        usuarioRepository.save(usuarioEntity);

        return fromTareaEntity(tareaEntity);
    }

    public TareaResponse completarTarea(UUID idTarea) {
        Optional<TareaEntity> optionalTareaEntity = tareaRepository.findById(idTarea);
        if(optionalTareaEntity.isEmpty()) {
            System.out.println("Registro no existe");
            return null;
        }

        TareaEntity te = optionalTareaEntity.get();
        te.setCompletado(true);
        tareaRepository.save(te);
        return fromTareaEntity(te);
    }
    // modificar el endpoint de completar tarea
    // se debe de reducir el numero de tareas activas del usuario asociado a la tarea
    public List<TareaResponse> findAll() {
        List<TareaEntity> tareaEntities = tareaRepository.findAll();
        List<TareaResponse> tareaResponses = new ArrayList<>();
        for(TareaEntity te: tareaEntities) {
            tareaResponses.add(fromTareaEntity(te));
        }
        return tareaResponses;
    }

    public TareaResponse cambiarTitulo(String nuevoTitulo, UUID idTarea) {
        if(!nuevoTitulo.matches("^[a-zA-Z0-9 ]+$")) {
            System.out.println("El titulo es incorrecto");
            // bad request
            return null;
        }

        Optional<TareaEntity> optionalTareaEntity = tareaRepository.findById(idTarea);

        if(optionalTareaEntity.isEmpty()) {
            System.out.println("No Encontrado");
            // not found
            return null;
        }
        TareaEntity tareaEntity = optionalTareaEntity.get();
        tareaEntity.setTitulo(nuevoTitulo);
        tareaRepository.save(tareaEntity);
        return fromTareaEntity(tareaEntity);
    }

    public List<TareaResponse> findTareasUsuario(UUID usuarioId) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(usuarioId).orElse(null);
        if(!usuarioRepository.existsById(usuarioId)){
            System.out.println("El usuario no existe");
            return null;
        }

        List<TareaEntity> tareas = tareaRepository.findByIdUsuario(usuarioId);
        List<TareaResponse> tareaResponses = new ArrayList<>();
        for(TareaEntity te: tareas) {
            TareaResponse tr = fromTareaEntity(te);
            tareaResponses.add(tr);
        }
        return tareaResponses;
    }

    private TareaResponse fromTareaEntity(TareaEntity tareaEntity) {
        return new TareaResponse(
                tareaEntity.getId(),
                tareaEntity.getTitulo(),
                tareaEntity.isCompletado(),
                tareaEntity.getDescripcion(),
                tareaEntity.getFechaFinalizacion(),
                tareaEntity.getUsuarioEntity().getId()
        );
    }
}

// crear un endpoint donde podamos cambiar el titulo de una tarea