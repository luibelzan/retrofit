package com.celnet.retrofit.model;

import com.celnet.retrofit.model.id.TGeneralId;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_general")
@IdClass(TGeneralId.class) // Vinculación con la clave compuesta
public class TGeneral {

    @Id
    @Column(name = "cod_distribuidora", length = 3, nullable = false)
    private String codDistribuidora;

    @Id
    @Column(name = "id_contador", length = 20, nullable = false, unique = true)
    private String idContador;

    @Column(name = "tip_equipo", length = 2, nullable = false)
    private String tipEquipo;

    @Column(name = "fec_envio")
    private Date fecEnvio;

    //Garantia
    @Column(name = "fec_averia")
    private Date fecAveria;

    @Column(name = "des_averia", length = 200)
    private String desAveria;

    //Garantia
    @Column(name = "des_observaciones", length = 200)
    private String desObservaciones;

    @Column(name = "fec_carga")
    private Date fecCarga;

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

    public String getTipEquipo() {
        return tipEquipo;
    }

    public void setTipEquipo(String tipEquipo) {
        this.tipEquipo = tipEquipo;
    }

    public Date getFecEnvio() {
        return fecEnvio;
    }

    public void setFecEnvio(Date fecEnvio) {
        this.fecEnvio = fecEnvio;
    }

    public Date getFecAveria() {
        return fecAveria;
    }

    public void setFecAveria(Date fecAveria) {
        this.fecAveria = fecAveria;
    }

    public String getDesAveria() {
        return desAveria;
    }

    public void setDesAveria(String desAveria) {
        this.desAveria = desAveria;
    }

    public String getDesObservaciones() {
        return desObservaciones;
    }

    public void setDesObservaciones(String desObservaciones) {
        this.desObservaciones = desObservaciones;
    }

    public Date getFecCarga() {
        return fecCarga;
    }

    public void setFecCarga(Date fecCarga) {
        this.fecCarga = fecCarga;
    }
}