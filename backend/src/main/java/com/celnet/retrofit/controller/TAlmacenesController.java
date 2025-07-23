package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TAlmacenes;
import com.celnet.retrofit.model.id.TAlmacenesId;
import com.celnet.retrofit.service.TAlmacenesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/almacenes")
public class TAlmacenesController {

    @Autowired
    private TAlmacenesService tAlmacenesService;

    @GetMapping
    public List<TAlmacenes> getAllAlmacenes() {
        return tAlmacenesService.findAll();
    }

    @GetMapping("/{codDistribuidora}/{codAlmacen}")
    public ResponseEntity<TAlmacenes> getAlmacenById(
            @PathVariable String codDistribuidora,
            @PathVariable Integer codAlmacen) {
        TAlmacenesId id = new TAlmacenesId(codDistribuidora, codAlmacen);
        return tAlmacenesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TAlmacenes createAlmacen(@RequestBody TAlmacenes almacen) {
        return tAlmacenesService.save(almacen);
    }

    @PutMapping
    public ResponseEntity<TAlmacenes> updateAlmacen(@RequestBody TAlmacenes almacen) {
        try {
            return ResponseEntity.ok(tAlmacenesService.update(almacen));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codDistribuidora}/{codAlmacen}")
    public ResponseEntity<Void> deleteAlmacen(
            @PathVariable String codDistribuidora,
            @PathVariable Integer codAlmacen) {
        TAlmacenesId id = new TAlmacenesId(codDistribuidora, codAlmacen);
        if (tAlmacenesService.findById(id).isPresent()) {
            tAlmacenesService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
