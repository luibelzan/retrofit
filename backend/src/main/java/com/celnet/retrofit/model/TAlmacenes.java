package com.celnet.retrofit.model;

import com.celnet.retrofit.model.id.TAlmacenesId;
import jakarta.persistence.*;

@Entity
@Table(name = "t_almacenes")
@IdClass(TAlmacenesId.class)
public class TAlmacenes {
    @Id
    @Column(name = "cod_distribuidora", nullable = false, length = 3)
    private String codDistribuidora;

    @Id
    @Column(name = "cod_almacen", nullable = false)
    private Integer codAlmacen;

    @Column(name = "des_almacen", length = 30)
    private String desAlmacen;

    @Column(name = "dir_almacen", length = 200)
    private String dirAlmacen;

    @Column(name = "cp_almacen", length = 5)
    private String cpAlmacen;

    @Column(name = "nom_encargado", length = 40)
    private String nomEncargado;

    public String getTelEncargado() {
        return telEncargado;
    }

    public void setTelEncargado(String telEncargado) {
        this.telEncargado = telEncargado;
    }

    public String getNomEncargado() {
        return nomEncargado;
    }

    public void setNomEncargado(String nomEncargado) {
        this.nomEncargado = nomEncargado;
    }

    public String getCpAlmacen() {
        return cpAlmacen;
    }

    public void setCpAlmacen(String cpAlmacen) {
        this.cpAlmacen = cpAlmacen;
    }

    public String getDirAlmacen() {
        return dirAlmacen;
    }

    public void setDirAlmacen(String dirAlmacen) {
        this.dirAlmacen = dirAlmacen;
    }

    public String getDesAlmacen() {
        return desAlmacen;
    }

    public void setDesAlmacen(String desAlmacen) {
        this.desAlmacen = desAlmacen;
    }

    public Integer getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(Integer codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getCodDistribuidora() {
        return codDistribuidora;
    }

    public void setCodDistribuidora(String codDistribuidora) {
        this.codDistribuidora = codDistribuidora;
    }

    @Column(name = "tel_encargado", length = 30)
    private String telEncargado;
}

