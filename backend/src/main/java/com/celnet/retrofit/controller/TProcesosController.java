package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.id.TProcesosId;
import com.celnet.retrofit.service.TProcesosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/procesos")
public class TProcesosController {

    @Autowired
    private TProcesosService procesosService;

    @GetMapping
    public ResponseEntity<List<TProcesos>> getAllProcesos() {
        List<TProcesos> procesos = procesosService.findAll();
        return ResponseEntity.ok(procesos);
    }

    @GetMapping("/{codDistribuidora}/{idContador}/{fecRecepcion}")
    public ResponseEntity<TProcesos> getProcesoById(
            @PathVariable String codDistribuidora,
            @PathVariable String idContador,
            @PathVariable String fecRecepcion) {

        TProcesosId id = new TProcesosId(codDistribuidora, idContador, java.sql.Date.valueOf(fecRecepcion));
        return procesosService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<TProcesos> createProceso(@RequestBody TProcesos procesos) {
        TProcesos createdProceso = procesosService.save(procesos);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProceso);
    }

    @PutMapping("/{codDistribuidora}/{idContador}/{fecRecepcion}")
    public ResponseEntity<TProcesos> updateProceso(
            @PathVariable String codDistribuidora,
            @PathVariable String idContador,
            @PathVariable String fecRecepcion,
            @RequestBody TProcesos procesos) {

        TProcesosId id = new TProcesosId(codDistribuidora, idContador, java.sql.Date.valueOf(fecRecepcion));
        if (!procesosService.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        procesos.setCodDistribuidora(codDistribuidora);
        procesos.setIdContador(idContador);
        procesos.setFecRecepcion(java.sql.Date.valueOf(fecRecepcion));
        TProcesos updatedProceso = procesosService.save(procesos);
        return ResponseEntity.ok(updatedProceso);
    }

    
    
}