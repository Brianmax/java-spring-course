package com.codigo.codigo.service;

import com.codigo.codigo.dto.UsuarioRequest;
import com.codigo.codigo.dto.UsuarioResponse;
import com.codigo.codigo.entity.UsuarioEntity;
import com.codigo.codigo.exception.DuplicateResourceException;
import com.codigo.codigo.reniec.ReniecClient;
import com.codigo.codigo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ReniecClient reniecClient;

    // Inyección por constructor: Spring nos entrega el repositorio ya creado.
    public UsuarioService(UsuarioRepository usuarioRepository, ReniecClient reniecClient) {
        this.usuarioRepository = usuarioRepository;
        this.reniecClient = reniecClient;
    }

    public UsuarioResponse saveUsuario(UsuarioRequest usuarioRequest) {
        String username = usuarioRequest.getUsername().trim();
        String email = usuarioRequest.getEmail().trim().toLowerCase();
        if (usuarioRepository.existsByUsername(username)) {
            System.out.println("El username ya existe");
            FieldError fieldError = new FieldError(
                    UsuarioRequest.class.toString(),
                    "username",
                    "El username ya existe"
            );
            throw new DuplicateResourceException("El username ya existe");
        }
        if (usuarioRepository.existsByEmail(email)) {
            System.out.println("El email ya existe");
            throw new DuplicateResourceException("El email ya existe");
        }

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setUsername(username);
//        // normalizar email
//        String emailRequest = usuarioRequest.getEmail();
//        usuarioRequest.setEmail(emailRequest.replace("@", "@ "));
        usuarioEntity.setEmail(email);
//        usuarioEntity.setNombre(usuarioRequest.getNombre().trim());
//        usuarioEntity.setApellido(usuarioRequest.getApellido().trim());
        usuarioEntity.setEdad(usuarioRequest.getEdad());
        usuarioEntity.setPassword(usuarioRequest.getPassword());

        UsuarioEntity usuarioGuardado = null;
        try {
            usuarioGuardado = usuarioRepository.save(usuarioEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            usuarioEntity.setId(null);
        }


        return toResponse(usuarioGuardado);
    }


    private UsuarioResponse toResponse(UsuarioEntity usuarioEntity) {
        UsuarioResponse usuarioResponse = new UsuarioResponse();
        usuarioResponse.setId(usuarioEntity.getId());
        usuarioResponse.setUsername(usuarioEntity.getUsername());
        usuarioResponse.setEmail(usuarioEntity.getEmail());
        usuarioResponse.setNombre(usuarioEntity.getNombre());
        usuarioResponse.setApellido(usuarioEntity.getApellido());
        usuarioResponse.setEdad(usuarioEntity.getEdad());
        return usuarioResponse;
    }
}
