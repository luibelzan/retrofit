package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TModelos;
import com.celnet.retrofit.model.id.TModelosId;
import com.celnet.retrofit.service.TModelosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/modelos")
public class TModelosController {

    @Autowired
    private TModelosService tmodelosService;

    @GetMapping
    public ResponseEntity<List<TModelos>> getAllModelos() {
        List<TModelos> modelos = tmodelosService.findAll();
        return modelos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(modelos);
    }

    @GetMapping("/{codModelo}/{codFabricante}")
    public ResponseEntity<TModelos> getModeloById(
            @PathVariable String codModelo,
            @PathVariable String codFabricante)
            {
        TModelosId id = new TModelosId(codModelo, codFabricante);
        return tmodelosService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TModelos> createModelo(@RequestBody TModelos modelo) {
        try {
            TModelos savedModelo = tmodelosService.save(modelo);
            return ResponseEntity.ok(savedModelo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<TModelos> updateModelo(@RequestBody TModelos modelo) {
        TModelosId id = new TModelosId(modelo.getCodModelo(), modelo.getCodFabricante());
        if (tmodelosService.findById(id).isPresent()) {
            try {
                TModelos updatedModelo = tmodelosService.update(modelo);
                return ResponseEntity.ok(updatedModelo);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codModelo}/{codFabricante}")
    public ResponseEntity<Void> deleteModelo(
            @PathVariable String codModelo,
            @PathVariable String codFabricante) {
        TModelosId id = new TModelosId(codModelo, codFabricante);
        if (tmodelosService.findById(id).isPresent()) {
            tmodelosService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}