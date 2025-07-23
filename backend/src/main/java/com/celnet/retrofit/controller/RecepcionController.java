package com.celnet.retrofit.controller;

import com.celnet.retrofit.service.RecepcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, String>> comprobarRecepcion(@RequestBody Map<String, String> datos) throws Exception {
        String distribuidora = datos.get("distribuidora");
        String tipoEquipo = datos.get("tipoEquipo");
        String almacen = datos.get("almacen");
        String codigoBarras = datos.get("codigoBarras");

        // Aquí se hace la comprobación
        String message = recepcionService.procesarRecepcion(distribuidora, tipoEquipo, almacen, codigoBarras);

        // Devolvemos el mensaje tal cual, no modificamos el mensaje aquí
        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.ok(response);
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
}