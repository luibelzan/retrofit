package com.celnet.retrofit.dto;


public interface ContadoresPorEnviar {
    String getCodDistribuidora();

    Integer getCodAlmacen();

    String getDesAlmacen();

    Integer getContadoresEnviados();

    Integer getContadoresRecepcion();

    Integer getContadoresRecuperados();

    Integer getContadoresSustituiblesGarantia();

    Integer getContadoresSinGarantia();

    Integer getTotalEquiposADevolver();
}
