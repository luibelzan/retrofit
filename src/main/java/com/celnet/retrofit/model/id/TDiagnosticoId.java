package com.celnet.retrofit.model.id;

import java.io.Serializable;
import java.util.Objects;

public class TDiagnosticoId implements Serializable {
    private String tipDiagnostico;
    private Integer codDiagnostico;

    public String getTipDiagnostico() {
        return tipDiagnostico;
    }

    public void setTipDiagnostico(String tipDiagnostico) {
        this.tipDiagnostico = tipDiagnostico;
    }

    public Integer getCodDiagnostico() {
        return codDiagnostico;
    }

    public void setCodDiagnostico(Integer codDiagnostico) {
        this.codDiagnostico = codDiagnostico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDiagnosticoId that = (TDiagnosticoId) o;
        return Objects.equals(tipDiagnostico, that.tipDiagnostico) && Objects.equals(codDiagnostico, that.codDiagnostico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipDiagnostico, codDiagnostico);
    }

    public TDiagnosticoId(String tipDiagnostico, Integer codDiagnostico) {
        this.tipDiagnostico = tipDiagnostico;
        this.codDiagnostico = codDiagnostico;
    }

    public TDiagnosticoId() {
    }
}

