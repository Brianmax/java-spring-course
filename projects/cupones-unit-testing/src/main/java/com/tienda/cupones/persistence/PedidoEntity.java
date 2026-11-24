package com.tienda.cupones.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "subtotal", nullable = false)
    private int subtotal;

    @Column(name = "descuento_aplicado", nullable = false)
    private int descuentoAplicado;

    @Column(name = "total_final", nullable = false)
    private int totalFinal;

    @Column(name = "codigo_cupon_aplicado", length = 64)
    private String codigoCuponAplicado;

    @Column(name = "envio_gratis", nullable = false)
    private boolean envioGratis;

    protected PedidoEntity() {
    }

    public PedidoEntity(
            String id,
            int subtotal,
            int descuentoAplicado,
            int totalFinal,
            String codigoCuponAplicado,
            boolean envioGratis
    ) {
        this.id = id;
        this.subtotal = subtotal;
        this.descuentoAplicado = descuentoAplicado;
        this.totalFinal = totalFinal;
        this.codigoCuponAplicado = codigoCuponAplicado;
        this.envioGratis = envioGratis;
    }

    public String getId() {
        return id;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public int getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public int getTotalFinal() {
        return totalFinal;
    }

    public String getCodigoCuponAplicado() {
        return codigoCuponAplicado;
    }

    public boolean isEnvioGratis() {
        return envioGratis;
    }
}
