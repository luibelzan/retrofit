package com.celnet.retrofit.model.id;

import java.io.Serializable;
import java.util.Objects;

public class TSustitucionesId implements Serializable {
    private String codDistribuidora;
    private String idContador;

    public TSustitucionesId() {}

    public TSustitucionesId(String codDistribuidora, String idContador) {
        this.codDistribuidora = codDistribuidora;
        this.idContador = idContador;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TSustitucionesId)) return false;
        TSustitucionesId that = (TSustitucionesId) o;
        return Objects.equals(codDistribuidora, that.codDistribuidora) &&
               Objects.equals(idContador, that.idContador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codDistribuidora, idContador);
    }
}
