package com.codigo.codigo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    // La edad es opcional, por eso usamos Integer (puede ser null) y no int.
    private Integer edad;

    @Column(nullable = false)
    private String password;

    @Column(name = "tareas_activas")
    private int tareasActivas = 0;

    @OneToMany(mappedBy = "usuarioEntity", fetch = FetchType.LAZY)
    List<TareaEntity> tareaEntities;

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", edad=" + edad +
                '}';
    }
}
