package com.celnet.retrofit.model;

import com.celnet.retrofit.model.id.TProcesosId;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_proceso")
@IdClass(TProcesosId.class)
public class TProcesos {

    @Id
    @Column(name = "cod_distribuidora", length = 3, nullable = false)
    private String codDistribuidora;

    @Id
    @Column(name = "id_contador", length = 20, nullable = false)
    private String idContador;

    @Id
    @Column(name = "fec_recepcion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRecepcion;

    @Column(name = "tip_equipo", length = 2)
    private String tipEquipo;

    @Column(name = "cod_fabricante", length = 1)
    private String codFabricante;

    @Column(name = "cod_modelo", length = 2)
    private String codModelo;

    @Column(name = "ano_fabricacion", length = 4)
    private String anoFabricacion;

    @Column(name = "cod_almacen")
    private Integer codAlmacen;

    @Column(name = "fec_averia")
    @Temporal(TemporalType.DATE)
    private Date fecAveria;

    @Column(name = "des_averia", length = 250)
    private String desAveria;

    @Column(name = "des_observaciones", length = 250)
    private String desObservaciones;

    @Column(name = "tip_diagnostico", length = 2)
    private String tipDiagnostico = "RC";  // Valor por defecto "RC"

    @Column(name = "cod_diagnostico")
    private Integer codDiagnostico = 30;  // Valor por defecto 30

    @Column(name = "id_lote")
    private Integer idLote;

    @Column(name = "fec_proceso")
    @Temporal(TemporalType.DATE)
    private Date fecProceso;

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

    public Date getFecRecepcion() {
        return fecRecepcion;
    }

    public void setFecRecepcion(Date fecRecepcion) {
        this.fecRecepcion = fecRecepcion;
    }

    public String getTipEquipo() {
        return tipEquipo;
    }

    public void setTipEquipo(String tipEquipo) {
        this.tipEquipo = tipEquipo;
    }

    public String getCodFabricante() {
        return codFabricante;
    }

    public void setCodFabricante(String codFabricante) {
        this.codFabricante = codFabricante;
    }

    public String getCodModelo() {
        return codModelo;
    }

    public void setCodModelo(String codModelo) {
        this.codModelo = codModelo;
    }

    public String getAnoFabricacion() {
        return anoFabricacion;
    }

    public void setAnoFabricacion(String anoFabricacion) {
        this.anoFabricacion = anoFabricacion;
    }

    public Integer getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(Integer codAlmacen) {
        this.codAlmacen = codAlmacen;
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

    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    public Date getFecProceso() {
        return fecProceso;
    }

    public void setFecProceso(Date fecProceso) {
        this.fecProceso = fecProceso;
    }
}