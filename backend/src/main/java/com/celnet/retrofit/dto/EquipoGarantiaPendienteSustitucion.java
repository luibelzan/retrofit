package com.celnet.retrofit.dto;

import java.util.Date;

public interface EquipoGarantiaPendienteSustitucion {
    String getCodDistribuidora();

    String getIdContador();

    String getCodFabricante();

    String getCodModelo();

    Integer getAnoFabricacion();

    Date getFecRecepcion();

    String getCodAlmacen();

    String getDesAlmacen();

    Date getFecAveria();

    String getDesAveria();

    String getDesObservaciones();

    String getTipDiagnostico();

    String getDesDiagnostico();

    Date getFecProceso();

    Integer getCodDiagnostico();

    Integer getIdLote();

    String getIdContadorSust();
}
