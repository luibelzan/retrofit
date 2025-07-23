package com.celnet.retrofit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "t_fabricantes")
public class TFabricantes {
    @Id
    @Column(name = "cod_fabricante", nullable = false, length = 1)
    private String codFabricante;

    @Column(name = "des_fabricante", length = 20)
    private String desFabricante;

    @Column(name = "cod_fabricante_ib", length = 2)
    private String codFabricanteIb;

    public String getCodFabricante() {
        return codFabricante;
    }

    public void setCodFabricante(String codFabricante) {
        this.codFabricante = codFabricante;
    }

    public String getDesFabricante() {
        return desFabricante;
    }

    public void setDesFabricante(String desFabricante) {
        this.desFabricante = desFabricante;
    }

    public String getCodFabricanteIb() {
        return codFabricanteIb;
    }

    public void setCodFabricanteIb(String codFabricanteIb) {
        this.codFabricanteIb = codFabricanteIb;
    }
}

