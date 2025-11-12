package com.celnet.retrofit.controller;

import com.celnet.retrofit.service.RecepcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.celnet.retrofit.model.TAlmacenes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/recepcion")
public class RecepcionController {

    @Autowired
    private RecepcionService recepcionService;

   @PostMapping("/comprobar")
    public ResponseEntity<Map<String, String>> comprobarRecepcion(@RequestBody Map<String, String> datos) {
        Map<String, String> response = new HashMap<>();
        try {
            String distribuidora = datos.get("distribuidora");
            String tipoEquipo = datos.get("tipoEquipo");
            String almacen = datos.get("almacen");
            String codigoBarras = datos.get("codigoBarras");

            String codigoBarrasFixed;
            if (codigoBarras.endsWith("ME") && codigoBarras.length() == 20) {
                codigoBarrasFixed = codigoBarras.substring(0, codigoBarras.length() - 2);
            } else {
                codigoBarrasFixed = codigoBarras;
            }

            String message = recepcionService.procesarRecepcion(distribuidora, tipoEquipo, almacen, codigoBarrasFixed);

            response.put("message", message);
            response.put("codigoBarras", codigoBarrasFixed);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }



    @PostMapping("/enviar")
    public ResponseEntity<Map<String, Object>> enviarRecepcion(@RequestBody List<Map<String, String>> registros) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Asegúrate de que los registros no estén vacíos
            if (registros == null || registros.isEmpty()) {
                response.put("success", false);
                response.put("message", "No se han recibido registros para guardar.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Iterar sobre cada registro y llamar a guardarRecepcion individualmente
            for (Map<String, String> registro : registros) {
                String distribuidora = registro.get("distribuidora");
                String tipoEquipo = registro.get("tipoEquipo");
                String almacen = registro.get("almacen");
                String codigoBarras = registro.get("codigoBarras");

                // Verifica si los campos están correctamente llenados
                if (distribuidora == null || tipoEquipo == null || almacen == null || codigoBarras == null) {
                    throw new IllegalArgumentException("Algunos campos están vacíos.");
                }

                // Llamar al servicio para guardar cada registro
                recepcionService.guardarRecepcion(distribuidora, tipoEquipo, almacen, codigoBarras);
            }

            response.put("success", true);
            response.put("message", "Recepciones enviadas y guardadas correctamente.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al enviar las recepciones: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/almacenes")
    public ResponseEntity<List<TAlmacenes>> obtenerAlmacenesPorDistribuidora(@RequestParam String codDistribuidora) {
        List<TAlmacenes> almacenes = recepcionService.obtenerAlmacenesPorDistribuidora(codDistribuidora);
        return ResponseEntity.ok(almacenes);
    }

}