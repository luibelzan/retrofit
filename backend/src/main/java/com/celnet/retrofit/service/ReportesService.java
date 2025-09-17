package com.celnet.retrofit.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.nio.charset.StandardCharsets;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.TSustituciones;
import com.celnet.retrofit.repository.TProcesosRepository;
import com.celnet.retrofit.repository.TSustitucionesRepository;
import com.celnet.retrofit.repository.TDistribuidorasRepository;
import com.celnet.retrofit.dto.ReporteRecuperado;
import com.celnet.retrofit.dto.ReporteAchatarrado;


@Service
public class ReportesService {

    @Autowired
    private TProcesosRepository procesosRepository;

    @Autowired
    private TSustitucionesRepository sustitucionesRepository;

    @Autowired
    private TDistribuidorasRepository distribuidorasRepository;

    public ByteArrayInputStream generarExcel(String codDistribuidora, Date fechaInicio, Date fechaFin) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Query 1
            List<TProcesos> totalEquipos = procesosRepository.getTotalEquipos(codDistribuidora, fechaInicio, fechaFin);
            Sheet sheet1 = workbook.createSheet("Total Equipos");
            Row header1 = sheet1.createRow(0);
            header1.createCell(0).setCellValue("cod_distribuidora");
            header1.createCell(1).setCellValue("id_contador");
            header1.createCell(2).setCellValue("cod_fabricante");
            header1.createCell(3).setCellValue("cod_modelo");
            header1.createCell(4).setCellValue("ano_fabricacion");
            header1.createCell(5).setCellValue("fec_recepcion");
            header1.createCell(6).setCellValue("cod_almacen");
            header1.createCell(7).setCellValue("fec_averia");
            header1.createCell(8).setCellValue("des_averia");
            header1.createCell(9).setCellValue("des_observaciones");
            header1.createCell(10).setCellValue("tip_diagnostico");
            header1.createCell(11).setCellValue("fec_proceso");
            header1.createCell(12).setCellValue("cod_diagnostico");
            header1.createCell(13).setCellValue("id_lote");
            header1.createCell(14).setCellValue("id_contador_sust");

            // Crear estilo para fecha
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

            int rowIdx = 1;
            for (TProcesos proceso : totalEquipos) {
                Row row = sheet1.createRow(rowIdx);
                row.createCell(0).setCellValue(proceso.getCodDistribuidora());
                row.createCell(1).setCellValue(proceso.getIdContador());
                row.createCell(2).setCellValue(proceso.getCodFabricante());
                row.createCell(3).setCellValue(proceso.getCodModelo());
                row.createCell(4).setCellValue(proceso.getAnoFabricacion());
                Cell cell5 = row.createCell(5);
                if (proceso.getFecRecepcion() != null) {
                    cell5.setCellValue(proceso.getFecRecepcion());
                    cell5.setCellStyle(dateCellStyle);
                }
                row.createCell(6).setCellValue(proceso.getCodAlmacen());
                Cell cell7 = row.createCell(7);
                if (proceso.getFecAveria() != null) {
                    cell7.setCellValue(proceso.getFecAveria());
                    cell7.setCellStyle(dateCellStyle);
                }
                row.createCell(8).setCellValue(proceso.getDesAveria());
                row.createCell(9).setCellValue(proceso.getDesObservaciones());
                row.createCell(10).setCellValue(proceso.getTipDiagnostico());
                Cell cell11 = row.createCell(11);
                if (proceso.getFecProceso() != null) {
                    cell11.setCellValue(proceso.getFecProceso());
                    cell11.setCellStyle(dateCellStyle);
                }
                row.createCell(12).setCellValue(proceso.getCodDiagnostico());
                row.createCell(13).setCellValue(
                        proceso.getIdLote() != null ? proceso.getIdLote() : 0);
                row.createCell(14).setCellValue(proceso.getIdContadorSust());
                rowIdx++;
            }

            // Query 2
            List<TProcesos> recuperados = procesosRepository.getRecuperados(codDistribuidora, fechaInicio, fechaFin);
            Sheet sheet2 = workbook.createSheet("Recuperados");
            Row header2 = sheet2.createRow(0);
            header2.createCell(0).setCellValue("cod_distribuidora");
            header2.createCell(1).setCellValue("id_contador");
            header2.createCell(2).setCellValue("cod_fabricante");
            header2.createCell(3).setCellValue("cod_modelo");
            header2.createCell(4).setCellValue("ano_fabricacion");
            header2.createCell(5).setCellValue("fec_recepcion");
            header2.createCell(6).setCellValue("cod_almacen");
            header2.createCell(7).setCellValue("fec_averia");
            header2.createCell(8).setCellValue("des_averia");
            header2.createCell(9).setCellValue("des_observaciones");
            header2.createCell(10).setCellValue("tip_diagnostico");
            header2.createCell(11).setCellValue("fec_proceso");
            header2.createCell(12).setCellValue("cod_diagnostico");
            header2.createCell(13).setCellValue("id_lote");

            rowIdx = 1;
            for (TProcesos proceso : recuperados) {
                Row row = sheet2.createRow(rowIdx);
                row.createCell(0).setCellValue(proceso.getCodDistribuidora());
                row.createCell(1).setCellValue(proceso.getIdContador());
                row.createCell(2).setCellValue(proceso.getCodFabricante());
                row.createCell(3).setCellValue(proceso.getCodModelo());
                row.createCell(4).setCellValue(proceso.getAnoFabricacion());

                // Fecha de recepción
                Cell cell5 = row.createCell(5);
                if (proceso.getFecRecepcion() != null) {
                    cell5.setCellValue(proceso.getFecRecepcion());
                    cell5.setCellStyle(dateCellStyle);
                }

                row.createCell(6).setCellValue(proceso.getCodAlmacen());

                // Fecha de avería
                Cell cell7 = row.createCell(7);
                if (proceso.getFecAveria() != null) {
                    cell7.setCellValue(proceso.getFecAveria());
                    cell7.setCellStyle(dateCellStyle);
                }

                row.createCell(8).setCellValue(proceso.getDesAveria());
                row.createCell(9).setCellValue(proceso.getDesObservaciones());
                row.createCell(10).setCellValue(proceso.getTipDiagnostico());

                // Fecha de proceso
                Cell cell11 = row.createCell(11);
                if (proceso.getFecProceso() != null) {
                    cell11.setCellValue(proceso.getFecProceso());
                    cell11.setCellStyle(dateCellStyle);
                }

                row.createCell(12).setCellValue(proceso.getCodDiagnostico());
                row.createCell(13).setCellValue(
                        proceso.getIdLote() != null ? proceso.getIdLote() : 0);

                rowIdx++;
            }

            // Query 3
            List<TProcesos> sustituidos = procesosRepository.getSustituidos(codDistribuidora, fechaInicio, fechaFin);
            Sheet sheet3 = workbook.createSheet("Sustituidos");
            Row header3 = sheet3.createRow(0);
            header3.createCell(0).setCellValue("cod_distribuidora");
            header3.createCell(1).setCellValue("id_contador");
            header3.createCell(2).setCellValue("cod_fabricante");
            header3.createCell(3).setCellValue("cod_modelo");
            header3.createCell(4).setCellValue("ano_fabricacion");
            header3.createCell(5).setCellValue("fec_recepcion");
            header3.createCell(6).setCellValue("cod_almacen");
            header3.createCell(7).setCellValue("fec_averia");
            header3.createCell(8).setCellValue("des_averia");
            header3.createCell(9).setCellValue("des_observaciones");
            header3.createCell(10).setCellValue("tip_diagnostico");
            header3.createCell(11).setCellValue("fec_proceso");
            header3.createCell(12).setCellValue("cod_diagnostico");
            header3.createCell(13).setCellValue("id_lote");
            header3.createCell(14).setCellValue("id_contador_sust");

            rowIdx = 1;
            for (TProcesos proceso : sustituidos) {
                Row row = sheet3.createRow(rowIdx);
                row.createCell(0).setCellValue(proceso.getCodDistribuidora());
                row.createCell(1).setCellValue(proceso.getIdContador());
                row.createCell(2).setCellValue(proceso.getCodFabricante());
                row.createCell(3).setCellValue(proceso.getCodModelo());
                row.createCell(4).setCellValue(proceso.getAnoFabricacion());

                // Fecha de recepción
                Cell cell5 = row.createCell(5);
                if (proceso.getFecRecepcion() != null) {
                    cell5.setCellValue(proceso.getFecRecepcion());
                    cell5.setCellStyle(dateCellStyle);
                }

                row.createCell(6).setCellValue(proceso.getCodAlmacen());

                // Fecha de avería
                Cell cell7 = row.createCell(7);
                if (proceso.getFecAveria() != null) {
                    cell7.setCellValue(proceso.getFecAveria());
                    cell7.setCellStyle(dateCellStyle);
                }

                row.createCell(8).setCellValue(proceso.getDesAveria());
                row.createCell(9).setCellValue(proceso.getDesObservaciones());
                row.createCell(10).setCellValue(proceso.getTipDiagnostico());

                // Fecha de proceso
                Cell cell11 = row.createCell(11);
                if (proceso.getFecProceso() != null) {
                    cell11.setCellValue(proceso.getFecProceso());
                    cell11.setCellStyle(dateCellStyle);
                }

                row.createCell(12).setCellValue(proceso.getCodDiagnostico());
                row.createCell(13).setCellValue(
                        proceso.getIdLote() != null ? proceso.getIdLote() : 0);

                // Nuevo campo: id_contador_sust
                row.createCell(14).setCellValue(
                        proceso.getIdContadorSust() != null ? proceso.getIdContadorSust() : "");

                rowIdx++;
            }

            // Query 4
            List<TSustituciones> sustituciones = sustitucionesRepository.getSustituciones(codDistribuidora);
            Sheet sheet4 = workbook.createSheet("Sustituciones");
            Row header4 = sheet4.createRow(0);
            header4.createCell(0).setCellValue("cod_distribuidora");
            header4.createCell(1).setCellValue("id_contador");
            header4.createCell(2).setCellValue("id_contador_sust");

            rowIdx = 1;
            for (TSustituciones s : sustituciones) {
                Row row = sheet4.createRow(rowIdx);
                row.createCell(0).setCellValue(s.getCodDistribuidora());
                row.createCell(1).setCellValue(s.getIdContador());
                row.createCell(2).setCellValue(s.getIdContadorSust());
                rowIdx++;
            }

            // Query 5
            List<TProcesos> danoFisico = procesosRepository.getDanoFisico(codDistribuidora, fechaInicio, fechaFin);
            Sheet sheet5 = workbook.createSheet("Daño fisico");
            Row header5 = sheet5.createRow(0);
            header5.createCell(0).setCellValue("cod_distribuidora");
            header5.createCell(1).setCellValue("id_contador");
            header5.createCell(2).setCellValue("cod_fabricante");
            header5.createCell(3).setCellValue("cod_modelo");
            header5.createCell(4).setCellValue("ano_fabricacion");
            header5.createCell(5).setCellValue("fec_recepcion");
            header5.createCell(6).setCellValue("cod_almacen");
            header5.createCell(7).setCellValue("fec_averia");
            header5.createCell(8).setCellValue("des_averia");
            header5.createCell(9).setCellValue("des_observaciones");
            header5.createCell(10).setCellValue("tip_diagnostico");
            header5.createCell(11).setCellValue("fec_proceso");
            header5.createCell(12).setCellValue("cod_diagnostico");

            // Rellenar filas con los datos
            rowIdx = 1;
            for (TProcesos p : danoFisico) {
                Row row = sheet5.createRow(rowIdx);
                row.createCell(0).setCellValue(p.getCodDistribuidora());
                row.createCell(1).setCellValue(p.getIdContador());
                row.createCell(2).setCellValue(p.getCodFabricante());
                row.createCell(3).setCellValue(p.getCodModelo());
                row.createCell(4).setCellValue(p.getAnoFabricacion());
                Cell cell5 = row.createCell(5);
                if (p.getFecRecepcion() != null) {
                    cell5.setCellValue(p.getFecRecepcion());
                    cell5.setCellStyle(dateCellStyle);
                }
                row.createCell(6).setCellValue(p.getCodAlmacen());
                Cell cell7 = row.createCell(7);
                if (p.getFecAveria() != null) {
                    cell7.setCellValue(p.getFecAveria());
                    cell7.setCellStyle(dateCellStyle);
                }
                row.createCell(8).setCellValue(p.getDesAveria());
                row.createCell(9).setCellValue(p.getDesObservaciones());
                row.createCell(10).setCellValue(p.getTipDiagnostico());
                Cell cell11 = row.createCell(11);
                if (p.getFecProceso() != null) {
                    cell11.setCellValue(p.getFecProceso());
                    cell11.setCellStyle(dateCellStyle);
                }
                row.createCell(12).setCellValue(p.getCodDiagnostico());
                rowIdx++;
            }

            // Query 6
            List<TProcesos> pendientes = procesosRepository.getPendientes(codDistribuidora, fechaInicio, fechaFin);
            Sheet sheet6 = workbook.createSheet("Pendientes Gestionar");

            // Crear cabeceras
            Row header6 = sheet6.createRow(0);
            header6.createCell(0).setCellValue("cod_distribuidora");
            header6.createCell(1).setCellValue("id_contador");
            header6.createCell(2).setCellValue("cod_fabricante");
            header6.createCell(3).setCellValue("cod_modelo");
            header6.createCell(4).setCellValue("ano_fabricacion");
            header6.createCell(5).setCellValue("fec_recepcion");
            header6.createCell(6).setCellValue("cod_almacen");
            header6.createCell(7).setCellValue("fec_averia");
            header6.createCell(8).setCellValue("des_averia");
            header6.createCell(9).setCellValue("des_observaciones");
            header6.createCell(10).setCellValue("tip_diagnostico");
            header6.createCell(11).setCellValue("fec_proceso");
            header6.createCell(12).setCellValue("cod_diagnostico");

            // Rellenar filas con los datos
            rowIdx = 1;
            for (TProcesos p : pendientes) {
                Row row = sheet6.createRow(rowIdx);
                row.createCell(0).setCellValue(p.getCodDistribuidora());
                row.createCell(1).setCellValue(p.getIdContador());
                row.createCell(2).setCellValue(p.getCodFabricante());
                row.createCell(3).setCellValue(p.getCodModelo());
                row.createCell(4).setCellValue(p.getAnoFabricacion());
                Cell cell5 = row.createCell(5);
                if(p.getFecRecepcion() != null) {
                    cell5.setCellValue(p.getFecRecepcion());
                    cell5.setCellStyle(dateCellStyle);
                }
                row.createCell(6).setCellValue(p.getCodAlmacen());
                Cell cell7 = row.createCell(7);
                if(p.getFecAveria() != null) {
                    cell7.setCellValue(p.getFecAveria());
                    cell7.setCellStyle(dateCellStyle);
                }
                row.createCell(8).setCellValue(p.getDesAveria());
                row.createCell(9).setCellValue(p.getDesObservaciones());
                row.createCell(10).setCellValue(p.getTipDiagnostico());
                Cell cell11 = row.createCell(11);
                if(p.getFecProceso() != null) {
                    cell11.setCellValue(p.getFecProceso());
                    cell11.setCellStyle(dateCellStyle);
                }
                row.createCell(12).setCellValue(p.getCodDiagnostico());
                rowIdx++;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el Excel", e);
        }
    }

    public byte[] generarReporteRecepcion(String codDistribuidora, Date fechaInicio) {
        List<TProcesos> procesos = procesosRepository.getReporteRecepcionados(codDistribuidora, fechaInicio);

        StringBuilder sb = new StringBuilder();
        for (TProcesos p : procesos) {
            sb.append("").append(p.getIdContador()).append("\n"); ;
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] generarReporteRecuperados(String codDistribuidora, Date fechaInicio) {
        List<ReporteRecuperado> procesos = procesosRepository.getReporteRecuperados(codDistribuidora, fechaInicio);

        StringBuilder sb = new StringBuilder();
        for (ReporteRecuperado p : procesos) {
            sb.append("").append(p.getIdContador()).append("\n"); ;
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] generarReporteAchatarrados(String codDistribuidora, Date fechaInicio) {
        List<ReporteAchatarrado> procesos = procesosRepository.getReporteAchatarrados(codDistribuidora, fechaInicio);

        StringBuilder sb = new StringBuilder();
        for (ReporteAchatarrado p : procesos) {
            sb.append("").append(p.getIdContador()).append("\n"); ;
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public String getNomDistribuidora(String codDistribuidora) {
        String nombre = distribuidorasRepository.findNomDistribuidoraByCodDistribuidora(codDistribuidora);

        return nombre;
    } 

}
