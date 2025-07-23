package com.celnet.retrofit.controller;

import com.celnet.retrofit.dto.ETLDatos;
import com.celnet.retrofit.model.TGeneral;
import com.celnet.retrofit.service.ETLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ETLController {

    @Autowired
    private ETLService etlService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                                @RequestParam("codDistribuidora") String codDistribuidora) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Procesar el archivo utilizando el código de distribuidora
            List<TGeneral> registros = etlService.leerExcel(file, codDistribuidora);

            // Llamar a cargarDatos y obtener los resultados
            ETLDatos resultado = etlService.cargarDatos(file, codDistribuidora);

            if (resultado == null) {
                response.put("error", "El resultado de la carga de datos es nulo. Verifica el proceso de ETL.");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear la respuesta
            response.put("mensaje", "Registros insertados: " + resultado.getRegistrosInsertados() +
                    " Registros leidos: " + resultado.getRegistrosLeidos());

            return ResponseEntity.ok(response);
        } catch (IOException | ParseException e) {
            response.put("error", "Error al procesar el archivo: " + e.getMessage());
            System.out.println("Respuesta de error enviada: " + response); // Imprimir respuesta de error
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "Ocurrió un error inesperado: " + e.getMessage());
            System.out.println("Respuesta de error enviada: " + response); // Imprimir respuesta de error
            return ResponseEntity.status(500).body(response);
        }
    }
}