package com.celnet.retrofit.model;

import com.celnet.retrofit.model.id.TAlmacenesId;
import com.celnet.retrofit.model.id.TDiagnosticoId;
import jakarta.persistence.*;

@Entity
@Table(name = "t_diagnostico")
@IdClass(TDiagnosticoId.class)
public class TDiagnostico {
    @Id
    @Column(name = "tip_diagnostico", nullable = false, length = 2)
    private String tipDiagnostico;

    @Id
    @Column(name = "cod_diagnostico", nullable = false, length = 2)
    private Integer codDiagnostico;

    @Column(name = "des_diagnostico", length = 50)
    private String desDiagnostico;

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

    public String getDesDiagnostico() {
        return desDiagnostico;
    }

    public void setDesDiagnostico(String desDiagnostico) {
        this.desDiagnostico = desDiagnostico;
    }
}

