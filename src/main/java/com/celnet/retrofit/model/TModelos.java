package com.celnet.retrofit.model;

import com.celnet.retrofit.model.id.TAlmacenesId;
import com.celnet.retrofit.model.id.TModelosId;
import jakarta.persistence.*;

@Entity
@Table(name = "t_modelos")
@IdClass(TModelosId.class)
public class TModelos {
    @Id
    @Column(name = "cod_modelo", nullable = false, length = 2)
    private String codModelo;

    @Id
    @Column(name = "cod_fabricante", nullable = false, length = 1)
    private String codFabricante;

    @Column(name = "tip_contador")
    private Integer tipContador;

    @Column(name = "num_fases", length = 2)
    private String numFases;

    @Column(name = "tip_medida", length = 2)
    private String tipMedida;

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

    public String getNumFases() {
        return numFases;
    }

    public void setNumFases(String numFases) {
        this.numFases = numFases;
    }

    public String getTipMedida() {
        return tipMedida;
    }

    public void setTipMedida(String tipMedida) {
        this.tipMedida = tipMedida;
    }

    public Integer getTipContador() {
        return tipContador;
    }

    public void setTipContador(Integer tipContador) {
        this.tipContador = tipContador;
    }
}
