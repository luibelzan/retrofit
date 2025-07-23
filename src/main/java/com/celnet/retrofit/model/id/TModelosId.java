package com.celnet.retrofit.model.id;

import java.io.Serializable;
import java.util.Objects;

public class TModelosId implements Serializable {
    private String codModelo;
    private String codFabricante;

    public String getCodModelo() {
        return codModelo;
    }

    public void setCodModelo(String codModelo) {
        this.codModelo = codModelo;
    }

    public String getCodFabricante() {
        return codFabricante;
    }

    public void setCodFabricante(String codFabricante) {
        this.codFabricante = codFabricante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TModelosId that = (TModelosId) o;
        return Objects.equals(codModelo, that.codModelo) && Objects.equals(codFabricante, that.codFabricante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codModelo, codFabricante);
    }

    public TModelosId(String codModelo, String codFabricante) {
        this.codModelo = codModelo;
        this.codFabricante = codFabricante;
    }

    public TModelosId() {
    }
}