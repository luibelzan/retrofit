package com.celnet.retrofit.model;

import com.celnet.retrofit.model.id.TFicherosId;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_ficheros")
@IdClass(TFicherosId.class) // Vinculación con la clave compuesta
public class TFicheros {

    @Id
    @Column(name = "cod_distribuidora", length = 3, nullable = false)
    private String codDistribuidora;

    @Id
    @Column(name = "fec_carga", nullable = false)
    private LocalDateTime fecCarga;

    @Column(name = "nom_fichero", length = 50, nullable = false)
    private String nomFichero;

    @Column(name = "tam_fichero")
    private Float tamFichero;

    @Column(name = "num_registros")
    private Integer numRegistros;

    @Column(name = "num_cargados")
    private Integer numCargados;

    @Column(name = "num_invalidos")
    private Integer numInvalidos;

    @Column(name = "cod_usuario")
    private Integer codUsuario;

    @Column(name = "tip_ficheros", length = 20)
    private String tipFicheros;

    // Getters y Setters
    public String getCodDistribuidora() {
        return codDistribuidora;
    }

    public void setCodDistribuidora(String codDistribuidora) {
        this.codDistribuidora = codDistribuidora;
    }

    public LocalDateTime  getFecCarga() {
        return fecCarga;
    }

    public void setFecCarga(LocalDateTime  fecCarga) {
        this.fecCarga = fecCarga;
    }

    public String getNomFichero() {
        return nomFichero;
    }

    public void setNomFichero(String nomFichero) {
        this.nomFichero = nomFichero;
    }

    public Float getTamFichero() {
        return tamFichero;
    }

    public void setTamFichero(Float tamFichero) {
        this.tamFichero = tamFichero;
    }

    public Integer getNumRegistros() {
        return numRegistros;
    }

    public void setNumRegistros(Integer numRegistros) {
        this.numRegistros = numRegistros;
    }

    public Integer getNumCargados() {
        return numCargados;
    }

    public void setNumCargados(Integer numCargados) {
        this.numCargados = numCargados;
    }

    public Integer getNumInvalidos() {
        return numInvalidos;
    }

    public void setNumInvalidos(Integer numInvalidos) {
        this.numInvalidos = numInvalidos;
    }

    public Integer getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Integer codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getTipFicheros() {
        return tipFicheros;
    }

    public void setTipFicheros(String tipFicheros) {
        this.tipFicheros = tipFicheros;
    }
}