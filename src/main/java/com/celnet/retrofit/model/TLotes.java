package com.celnet.retrofit.model;

import com.celnet.retrofit.model.id.TLotesId;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_lotes")
@IdClass(TLotesId.class)
public class TLotes {

    // Los campos no deben tener @ManyToOne, sino que se mapean directamente como columnas.
    @Id
    @Column(name = "cod_distribuidora", nullable = false, length = 3)
    private String codDistribuidora;

    @Id
    @Column(name = "id_lote", nullable = false)
    private Integer idLote;

    @Column(name = "nom_lote", length = 15)
    private String nomLote;

    @Column(name = "cod_almacen")
    private Integer codAlmacen;

    @Column(name = "fec_lote")
    @Temporal(TemporalType.DATE)
    private Date fecLote;

    public TLotes() {}

    // Getters y setters
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

    public String getNomLote() {
        return nomLote;
    }

    public void setNomLote(String nomLote) {
        this.nomLote = nomLote;
    }

    public Integer getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(Integer codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public Date getFecLote() {
        return fecLote;
    }

    public void setFecLote(Date fecLote) {
        this.fecLote = fecLote;
    }
}