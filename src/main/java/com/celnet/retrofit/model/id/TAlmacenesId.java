package com.celnet.retrofit.model.id;

import java.io.Serializable;
import java.util.Objects;

public class TAlmacenesId implements Serializable {
    private String codDistribuidora;
    private Integer codAlmacen;

    public String getCodDistribuidora() {
        return codDistribuidora;
    }

    public void setCodDistribuidora(String codDistribuidora) {
        this.codDistribuidora = codDistribuidora;
    }

    public Integer getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(Integer codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TAlmacenesId that = (TAlmacenesId) o;
        return Objects.equals(codDistribuidora, that.codDistribuidora) && Objects.equals(codAlmacen, that.codAlmacen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codDistribuidora, codAlmacen);
    }

    public TAlmacenesId(String codDistribuidora, Integer codAlmacen) {
        this.codDistribuidora = codDistribuidora;
        this.codAlmacen = codAlmacen;
    }

    public TAlmacenesId() {
    }
}

