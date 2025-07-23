package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TDistribuidoras;
import com.celnet.retrofit.service.TDistribuidorasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/distribuidoras")
public class TDistribuidorasController {

    @Autowired
    private TDistribuidorasService tDistribuidorasService;

    @GetMapping
    public ResponseEntity<List<TDistribuidoras>> getAllDistribuidoras() {
        List<TDistribuidoras> distribuidoras = tDistribuidorasService.findAll();
        return distribuidoras.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(distribuidoras);
    }

    @GetMapping("/{codDistribuidora}")
    public ResponseEntity<TDistribuidoras> getDistribuidoraById(@PathVariable String codDistribuidora) {
        return tDistribuidorasService.findById(codDistribuidora)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TDistribuidoras> createDistribuidora(@RequestBody TDistribuidoras distribuidora) {
        try {
            TDistribuidoras savedDistribuidora = tDistribuidorasService.save(distribuidora);
            return ResponseEntity.ok(savedDistribuidora);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{codDistribuidora}")
    public ResponseEntity<TDistribuidoras> updateDistribuidora(
            @PathVariable String codDistribuidora, @RequestBody TDistribuidoras distribuidora) {
        try {
            if (!tDistribuidorasService.findById(codDistribuidora).isPresent()) {
                return ResponseEntity.notFound().build();
            }

            distribuidora.setCodDistribuidora(codDistribuidora);

            TDistribuidoras updatedDistribuidora = tDistribuidorasService.update(distribuidora);
            return ResponseEntity.ok(updatedDistribuidora);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{codDistribuidora}")
    public ResponseEntity<Void> deleteDistribuidora(@PathVariable String codDistribuidora) {
        if (tDistribuidorasService.findById(codDistribuidora).isPresent()) {
            tDistribuidorasService.deleteById(codDistribuidora);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}