package com.celnet.retrofit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import com.celnet.retrofit.service.ReportesService;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders="*", methods={RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/reportes")
public class ReportesController {

    @Autowired
    private ReportesService reportesService;

    @GetMapping("/landis/excel")
    public ResponseEntity<InputStreamResource> generarReporteExcel(
        @RequestParam String codDistribuidora,
        @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
        @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date fechaFin) {

        ByteArrayInputStream in = reportesService.generarExcel(codDistribuidora, fechaInicio, fechaFin);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Reporte.xlsx");

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(new InputStreamResource(in));
        }

    @GetMapping("/recepcion")
    public ResponseEntity<byte[]> generarReporteRecepcion(
        @RequestParam String codDistribuidora,
        @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date fechaInicio) {

        byte[] contenido = reportesService.generarReporteRecepcion(codDistribuidora, fechaInicio);

        String distribuidora = reportesService.getNomDistribuidora(codDistribuidora);

        Date hoy = new Date();
        String fechaFormateada = new java.text.SimpleDateFormat("yyyy_MM_dd").format(hoy);

        String nombreArchivo = distribuidora + "_Recibidos_" + fechaFormateada + ".txt";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION) // 👈 clave
                .contentType(MediaType.TEXT_PLAIN)
                .body(contenido);
    }

    @GetMapping("/recuperados")
    public ResponseEntity<byte[]> generarReporteRecuperados(
        @RequestParam String codDistribuidora,
        @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date fechaInicio) {

        byte[] contenido = reportesService.generarReporteRecuperados(codDistribuidora, fechaInicio);

        String distribuidora = reportesService.getNomDistribuidora(codDistribuidora);

        Date hoy = new Date();
        String fechaFormateada = new java.text.SimpleDateFormat("yyyy_MM_dd").format(hoy);

        String nombreArchivo = distribuidora + "_Recuperados_" + fechaFormateada + ".txt";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.TEXT_PLAIN)
                .body(contenido);
    }

    @GetMapping("/achatarrados")
    public ResponseEntity<byte[]> generarReporteAchatarrados(
        @RequestParam String codDistribuidora,
        @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date fechaInicio) {

        byte[] contenido = reportesService.generarReporteAchatarrados(codDistribuidora, fechaInicio);

        String distribuidora = reportesService.getNomDistribuidora(codDistribuidora);

        Date hoy = new Date();
        String fechaFormateada = new java.text.SimpleDateFormat("yyyy_MM_dd").format(hoy);

        String nombreArchivo = distribuidora + "_Chatarra_" + fechaFormateada + ".txt";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.TEXT_PLAIN)
                .body(contenido);
    }
    
}
