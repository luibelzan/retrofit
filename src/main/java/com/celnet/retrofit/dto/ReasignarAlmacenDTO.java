package com.celnet.retrofit.dto;

public class ReasignarAlmacenDTO {
    private String codigoBarras;
    private String nuevoAlmacen;

    // Getters y Setters
    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNuevoAlmacen() {
        return nuevoAlmacen;
    }

    public void setNuevoAlmacen(String nuevoAlmacen) {
        this.nuevoAlmacen = nuevoAlmacen;
    }
}