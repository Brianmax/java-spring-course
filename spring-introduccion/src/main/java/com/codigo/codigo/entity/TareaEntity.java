package com.codigo.codigo.entity;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.UUID;
@Entity
@Table(name = "tarea")
public class TareaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // generalo automaticamente
    private UUID id;
    private String titulo;
    private boolean completado;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    private String descripcion;
    @Column(name = "fecha_finalizacion")
    private Date fechaFinalizacion;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

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
