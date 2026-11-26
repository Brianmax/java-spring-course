package com.codigo.codigo.service;

import com.codigo.codigo.dto.UsuarioRequest;
import com.codigo.codigo.dto.UsuarioResponse;
import com.codigo.codigo.entity.UsuarioEntity;
import com.codigo.codigo.exception.DuplicateResourceException;
import com.codigo.codigo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository);
    }

    // happy path
    @Test
    void saveUsuarioNormalizadoYDevolverResponse() {
        // Arrange
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setUsername("george.maxi");
        usuarioRequest.setEmail("george.maxi@gmail.com");
        usuarioRequest.setEdad(29);
        usuarioRequest.setPassword("92999");

        when(usuarioRepository.existsByUsername("george.maxi")).thenReturn(false);
        when(usuarioRepository.existsByEmail("george.maxi@gmail.com")).thenReturn(false);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(invocation ->
        {
            UsuarioEntity usuarioEntity = invocation.getArgument(0);
            usuarioEntity.setId(UUID.randomUUID());
            return usuarioEntity;
        });

        // act
        UsuarioResponse usuarioResponse = usuarioService.saveUsuario(usuarioRequest);

        // assert
        assertNotNull(usuarioResponse.getId());
        assertEquals(usuarioResponse.getEmail(), usuarioRequest.getEmail());

    }

    // email duplicado
    @Test
    void duplicatedEmailSaveUsuario() {
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setUsername("george.maxi");
        usuarioRequest.setEmail("george.maxi@gmail.com");
        usuarioRequest.setEdad(29);
        usuarioRequest.setPassword("92999");

        when(usuarioRepository.existsByUsername("george.maxi")).thenReturn(false);
        when(usuarioRepository.existsByEmail("george.maxi@gmail.com")).thenReturn(true);

        DuplicateResourceException error =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> usuarioService.saveUsuario(usuarioRequest));

        assertEquals("El email ya existe", error.getMessage());
    }
}