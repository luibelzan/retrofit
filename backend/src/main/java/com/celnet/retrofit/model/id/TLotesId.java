package com.celnet.retrofit.model.id;

import java.io.Serializable;
import java.util.Objects;

public class TLotesId implements Serializable {

    private String codDistribuidora;
    private Integer idLote;

    public TLotesId() {}

    public TLotesId(String codDistribuidora, Integer idLote) {
        this.codDistribuidora = codDistribuidora;
        this.idLote = idLote;
    }

    // Getters y Setters
    public String getCodDistribuidora() {
        return codDistribuidora;
    }

    public void setCodDistribuidora(String codDistribuidora) {
        this.codDistribuidora = codDistribuidora;
    }

    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TLotesId tLotesId = (TLotesId) o;
        return Objects.equals(codDistribuidora, tLotesId.codDistribuidora) &&
                Objects.equals(idLote, tLotesId.idLote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codDistribuidora, idLote);
    }
}