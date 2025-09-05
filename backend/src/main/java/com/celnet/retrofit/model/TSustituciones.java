package com.celnet.retrofit.model;

import com.celnet.retrofit.model.id.TSustitucionesId;

import jakarta.persistence.*;

@Entity
@Table(name = "t_sustituciones") // <-- cambia por el nombre real de la tabla
@IdClass(TSustitucionesId.class)
public class TSustituciones {

    @Id
    @Column(name = "cod_distribuidora", nullable=false)
    private String codDistribuidora;

    @Id
    @Column(name = "id_contador", nullable=false)
    private String idContador;

    @Column(name="id_contador_sust", nullable=false)
    private String idContadorSust;

    public String getCodDistribuidora() {
        return codDistribuidora;
    }

    public void setCodDistribuidora(String codDistribuidora) {
        this.codDistribuidora = codDistribuidora;
    }

    public String getIdContador() {
        return idContador;
    }

    public void setIdContador(String idContador) {
        this.idContador = idContador;
    }

    public String getIdContadorSust() {
        return idContadorSust;
    }

    public void setIdContadorSust(String idContadorSust) {
        this.idContadorSust = idContadorSust;
    }
}
