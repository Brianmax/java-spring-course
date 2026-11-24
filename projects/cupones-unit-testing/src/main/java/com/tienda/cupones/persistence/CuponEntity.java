package com.tienda.cupones.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cupones")
public class CuponEntity {

    @Id
    @Column(name = "codigo", nullable = false, length = 64)
    private String codigo;

    @Column(name = "porcentaje", nullable = false)
    private int porcentaje;

    @Column(name = "minimo", nullable = false)
    private int minimo;

    @Column(name = "tope", nullable = false)
    private int tope;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    protected CuponEntity() {
    }

    public CuponEntity(String codigo, int porcentaje, int minimo, int tope, boolean activo) {
        this.codigo = codigo;
        this.porcentaje = porcentaje;
        this.minimo = minimo;
        this.tope = tope;
        this.activo = activo;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public int getMinimo() {
        return minimo;
    }

    public int getTope() {
        return tope;
    }

    public boolean isActivo() {
        return activo;
    }
}
