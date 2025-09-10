package com.celnet.retrofit.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.celnet.retrofit.dto.AchatarradosPorFallo;
import com.celnet.retrofit.dto.ContadoresEnviadosPorLote;
import com.celnet.retrofit.dto.EquipoGarantiaPendienteSustitucion;
import com.celnet.retrofit.dto.TasaEnvioAlmacen;
import com.celnet.retrofit.service.EstadisticasService;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/estadisticas")
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    @GetMapping("/equipos-garantia")
    public ResponseEntity<List<EquipoGarantiaPendienteSustitucion>> getEquiposGarantia(
        @RequestParam String codDistribuidora,
        @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date fechaProceso) {
            
            return ResponseEntity.ok(estadisticasService.getEquiposGarantia(codDistribuidora, fechaProceso));
        }

    @GetMapping("/achatarrados-fallo")
    public ResponseEntity<List<AchatarradosPorFallo>> getAchatarradosPorFallo(@RequestParam String codDistribuidora,
        @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date fechaProceso) {
        return ResponseEntity.ok(estadisticasService.getAchatarradosPorFallos(codDistribuidora, fechaProceso));
    }

    @GetMapping("/enviados-lote")
    public ResponseEntity<List<ContadoresEnviadosPorLote>> getEnviadosPorLote(@RequestParam String codDistribuidora, 
        @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date fechaProceso) {
        return ResponseEntity.ok(estadisticasService.getContadoresEnviadosPorLotes(codDistribuidora, fechaProceso));
    }

    @GetMapping("/tasa-enviados")
    public ResponseEntity<List<TasaEnvioAlmacen>> getTasaEnvioAlmacen(@RequestParam String codDistribuidora) {
        return ResponseEntity.ok(estadisticasService.getTasaEnvioAlmacen(codDistribuidora));
    }
    
    
}
