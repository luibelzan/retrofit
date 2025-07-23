package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TFabricantes;
import com.celnet.retrofit.service.TFabricantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/fabricantes")
public class TFabricantesController {

    @Autowired
    private TFabricantesService tFabricantesService;

    @GetMapping
    public List<TFabricantes> getAllFabricantes() {
        return tFabricantesService.findAll();
    }

    @GetMapping("/{codFabricante}")
    public ResponseEntity<TFabricantes> getFabricanteById(@PathVariable String codFabricante) {
        return tFabricantesService.findById(codFabricante)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TFabricantes createFabricante(@RequestBody TFabricantes fabricante) {
        return tFabricantesService.save(fabricante);
    }

    @PutMapping("/{codFabricante}")
    public ResponseEntity<TFabricantes> updateFabricante(
            @PathVariable String codFabricante,
            @RequestBody TFabricantes fabricante) {
        try {
            fabricante.setCodFabricante(codFabricante);
            return ResponseEntity.ok(tFabricantesService.update(fabricante));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codFabricante}")
    public ResponseEntity<Void> deleteFabricante(@PathVariable String codFabricante) {
        if (tFabricantesService.findById(codFabricante).isPresent()) {
            tFabricantesService.deleteById(codFabricante);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}