package com.celnet.retrofit.model.id;

import jakarta.persistence.EmbeddedId;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TProcesosId implements Serializable {

    private String codDistribuidora;
    private String idContador;
    private Date fecRecepcion;

    // Constructor sin argumentos
    public TProcesosId() {}

    public TProcesosId(String codDistribuidora, String idContador) {
        this.codDistribuidora = codDistribuidora;
        this.idContador = idContador;
    }

    // Constructor con argumentos
    public TProcesosId(String codDistribuidora, String idContador, Date fecRecepcion) {
        this.codDistribuidora = codDistribuidora;
        this.idContador = idContador;
        this.fecRecepcion = fecRecepcion;
    }

    // Getters y Setters
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

    public Date getFecRecepcion() {
        return fecRecepcion;
    }

    public void setFecRecepcion(Date fecRecepcion) {
        this.fecRecepcion = fecRecepcion;
    }

    // Sobrescribir equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TProcesosId procesoId = (TProcesosId) o;
        return codDistribuidora.equals(procesoId.codDistribuidora) &&
                idContador.equals(procesoId.idContador) &&
                fecRecepcion.equals(procesoId.fecRecepcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codDistribuidora, idContador, fecRecepcion);
    }
}