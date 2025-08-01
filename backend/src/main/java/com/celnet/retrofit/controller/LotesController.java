package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TAlmacenes;
import com.celnet.retrofit.model.TLotes;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.service.LotesService;
import com.celnet.retrofit.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.celnet.retrofit.service.TAlmacenesService;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/lotes")
public class LotesController {

    private final LotesService lotesService;
    private final BarcodeService barcodeService;
    private final TAlmacenesService almacenesService;


    @Autowired
    public LotesController(LotesService lotesService, BarcodeService barcodeService, TAlmacenesService almacenesService) {
        this.lotesService = lotesService;
        this.barcodeService = barcodeService;
        this.almacenesService = almacenesService;
    }

    // Crear un nuevo lote
    @PostMapping
    public ResponseEntity<TLotes> createLote(@RequestBody TLotes lote) {
        try {
            TLotes createdLote = lotesService.createLote(lote);
            return ResponseEntity.ok(createdLote);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Obtener todos los lotes
    @GetMapping
    public ResponseEntity<List<TLotes>> getAllLotes() {
        List<TLotes> lotes = lotesService.getAllLotes();
        return ResponseEntity.ok(lotes);
    }

    // Obtener un lote por ID compuesto
    @GetMapping("/{codDistribuidora}/{idLote}")
    public ResponseEntity<TLotes> getLoteById(@PathVariable String codDistribuidora, @PathVariable Integer idLote) {
        try {
            TLotes lote = lotesService.getLoteById(codDistribuidora, idLote);
            return ResponseEntity.ok(lote);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar un lote existente
    @PutMapping("/{codDistribuidora}/{idLote}")
    public ResponseEntity<TLotes> updateLote(@PathVariable String codDistribuidora, @PathVariable Integer idLote, @RequestBody TLotes loteDetalles) {
        try {
            TLotes updatedLote = lotesService.updateLote(codDistribuidora, idLote, loteDetalles);
            return ResponseEntity.ok(updatedLote);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Eliminar un lote por ID compuesto
    @DeleteMapping("/{codDistribuidora}/{idLote}")
    public ResponseEntity<Void> deleteLote(@PathVariable String codDistribuidora, @PathVariable Integer idLote) {
        try {
            lotesService.deleteLote(codDistribuidora, idLote);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Validar un contador antes de añadirlo al lote
    @PostMapping("/validarContador")
    public ResponseEntity<Map<String, String>> validarContador(@RequestParam("idContador") String idContador) {
        Map<String, String> response = new HashMap<>();
        try {
            String idContadorFixed;

            if (idContador.endsWith("ME") && idContador.length() == 20) {
                idContadorFixed = idContador.substring(0, idContador.length() - 2);
            } else {
                idContadorFixed = idContador;
            }

            lotesService.validarContadorRecepcionado(idContadorFixed);
            response.put("message", "Contador válido");
            response.put("idContador", idContadorFixed);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    // Asignar lote a los contadores seleccionados
    @PostMapping("/asignar")
    public ResponseEntity<String> asignarLote(@RequestParam("idLote") Integer idLote,
                                              @RequestParam("idContadores") String idContadores,
                                              @RequestParam("codDistribuidora") String codDistribuidora) {
        try {
            List<String> contadoresList = Arrays.asList(idContadores.split(","));
            String mensaje = lotesService.asignarLoteAContadores(idLote, contadoresList, codDistribuidora);
            return ResponseEntity.ok(mensaje);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener contadores por lote
    @GetMapping("/{codDistribuidora}/{idLote}/contadores")
    public ResponseEntity<List<TProcesos>> getContadoresPorLote(@PathVariable String codDistribuidora, @PathVariable Integer idLote) {
        try {
            List<TProcesos> contadores = lotesService.obtenerContadoresPorLote(codDistribuidora, idLote);
            return ResponseEntity.ok(contadores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Generar Excel con los códigos de barras para los contadores
    @GetMapping("/{codDistribuidora}/{idLote}/contadores/excel")
    public ResponseEntity<InputStreamResource> generarExcelConCodigosDeBarras(@PathVariable String codDistribuidora, @PathVariable Integer idLote) {
        try {
            // Obtener los contadores del lote
            List<TProcesos> contadores = lotesService.obtenerContadoresPorLote(codDistribuidora, idLote);

            // Obtener la descripción del almacén usando el codDistribuidora y codAlmacen
            String desAlmacen = almacenesService.obtenerDesAlmacen(codDistribuidora, idLote);

            // Generar el archivo Excel con los códigos de barras
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            barcodeService.generarExcelConCodigoBarras(contadores, outStream);

            // Establecer los encabezados para la descarga del archivo
            HttpHeaders headers = new HttpHeaders();
            String nombreArchivo = "LOTE_" + idLote + "_Almacen_" + desAlmacen + ".xlsx";
            headers.add("Content-Disposition", "attachment; filename=" + nombreArchivo);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(new ByteArrayInputStream(outStream.toByteArray())));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
