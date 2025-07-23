package com.celnet.retrofit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "t_distribuidoras")
public class TDistribuidoras {
    @Id
    @Column(name = "cod_distribuidora", nullable = false, length = 3)
    private String codDistribuidora;

    @Column(name = "nom_distribuidora", length = 30)
    private String nomDistribuidora;

    @Column(name = "tip_proceso", length = 1)
    private String tipProceso;

    @Column(name = "precio_contador")
    private Float precioContador;

    public String getCodDistribuidora() {
        return codDistribuidora;
    }

    public void setCodDistribuidora(String codDistribuidora) {
        this.codDistribuidora = codDistribuidora;
    }

    public String getNomDistribuidora() {
        return nomDistribuidora;
    }

    public void setNomDistribuidora(String nomDistribuidora) {
        this.nomDistribuidora = nomDistribuidora;
    }

    public String getTipProceso() {
        return tipProceso;
    }

    public void setTipProceso(String tipProceso) {
        this.tipProceso = tipProceso;
    }

    public Float getPrecioContador() {
        return precioContador;
    }

    public void setPrecioContador(Float precioContador) {
        this.precioContador = precioContador;
    }
}

