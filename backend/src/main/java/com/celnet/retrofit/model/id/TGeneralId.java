package com.celnet.retrofit.model.id;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class TGeneralId implements Serializable {
    private String codDistribuidora; // Primer campo de la clave
    private String idContador; // Segundo campo de la clave

    // Constructor vacío
    public TGeneralId() {}

    public TGeneralId(String id, Date date) {
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

    // equals() y hashCode() deben ser implementados
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TGeneralId)) return false;
        TGeneralId that = (TGeneralId) o;
        return codDistribuidora.equals(that.codDistribuidora) &&
                idContador.equals(that.idContador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codDistribuidora, idContador);
    }

    public TGeneralId(String codDistribuidora, String idContador) {
        this.codDistribuidora = codDistribuidora;
        this.idContador = idContador;
    }
}