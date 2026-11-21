package com.codigo.codigo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;
@Entity
@Table(name = "tarea")
@Getter
@Setter
public class TareaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // generalo automaticamente
    private UUID id;
    private String titulo;
    private boolean completado = false;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion = new Date(System.currentTimeMillis());
    private String descripcion;
    @Column(name = "fecha_finalizacion")
    private Date fechaFinalizacion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuarioEntity;

    @Override
    public String toString() {
        return "TareaEntity{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", completado=" + completado +
                ", fechaCreacion=" + fechaCreacion +
                ", descripcion='" + descripcion + '\'' +
                ", fechaFinalizacion=" + fechaFinalizacion +
                '}';
    }
}

// insert into
