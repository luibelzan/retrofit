package com.celnet.retrofit.model.id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class TFicherosId implements Serializable {

    private String codDistribuidora;
    private LocalDateTime fecCarga;  // Cambiado a LocalDateTime

    public TFicherosId() {}

    public TFicherosId(String codDistribuidora, LocalDateTime fecCarga) {
        this.codDistribuidora = codDistribuidora;
        this.fecCarga = fecCarga;
    }

    // Getters y Setters
    public String getCodDistribuidora() {
        return codDistribuidora;
    }

    public void setCodDistribuidora(String codDistribuidora) {
        this.codDistribuidora = codDistribuidora;
    }

    public LocalDateTime getFecCarga() {
        return fecCarga;
    }

    public void setFecCarga(LocalDateTime fecCarga) {
        this.fecCarga = fecCarga;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TFicherosId)) return false;
        TFicherosId that = (TFicherosId) o;
        return Objects.equals(codDistribuidora, that.codDistribuidora) &&
                Objects.equals(fecCarga, that.fecCarga);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codDistribuidora, fecCarga);
    }
}