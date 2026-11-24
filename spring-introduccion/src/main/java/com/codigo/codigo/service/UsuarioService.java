package com.codigo.codigo.service;

import com.codigo.codigo.dto.UsuarioRequest;
import com.codigo.codigo.dto.UsuarioResponse;
import com.codigo.codigo.entity.UsuarioEntity;
import com.codigo.codigo.exception.DuplicateResourceException;
import com.codigo.codigo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor: Spring nos entrega el repositorio ya creado.
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse saveUsuario(UsuarioRequest usuarioRequest) {
        if (!esValido(usuarioRequest)) {
            System.out.println("Datos del usuario incorrectos");
            return null;
            // 404, 409, 400
            // 404 --> not found
            // 409 --> ya existe un usuario con el mismo valor
            // 400 --> se incumple una regla
        }

        String username = usuarioRequest.getUsername().trim();
        String email = usuarioRequest.getEmail().trim().toLowerCase();

        if (usuarioRepository.existsByUsername(username)) {
            System.out.println("El username ya existe");
            throw new DuplicateResourceException("El username ya existe");
        }
        if (usuarioRepository.existsByEmail(email)) {
            System.out.println("El email ya existe");
            throw new DuplicateResourceException("El email ya existe");
        }

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setUsername(username);
        usuarioEntity.setEmail(email);
        usuarioEntity.setNombre(usuarioRequest.getNombre().trim());
        usuarioEntity.setApellido(usuarioRequest.getApellido().trim());
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

    private boolean esValido(UsuarioRequest usuarioRequest) {
        if (usuarioRequest == null) {
            return false;
        }
        if (usuarioRequest.getUsername() == null || usuarioRequest.getUsername().isBlank()) {
            return false;
        }
        if (usuarioRequest.getEmail() == null || usuarioRequest.getEmail().isBlank()
                || !usuarioRequest.getEmail().contains("@")) {
            return false;
        }
        if (usuarioRequest.getNombre() == null || usuarioRequest.getNombre().isBlank()) {
            return false;
        }
        if (usuarioRequest.getApellido() == null || usuarioRequest.getApellido().isBlank()) {
            return false;
        }
        if (usuarioRequest.getPassword() == null || usuarioRequest.getPassword().isBlank()) {
            return false;
        }
        // La edad es opcional, pero si viene debe ser válida.
        if (usuarioRequest.getEdad() != null && usuarioRequest.getEdad() < 0) {
            return false;
        }
        return true;
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
