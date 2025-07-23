package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TFicheros;
import com.celnet.retrofit.model.id.TFicherosId;
import com.celnet.retrofit.service.TFicherosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/t_ficheros")
public class TFicherosController {

    private final TFicherosService tFicherosService;

    @Autowired
    public TFicherosController(TFicherosService tFicherosService) {
        this.tFicherosService = tFicherosService;
    }

    @GetMapping
    public List<TFicheros> getAllTFicheros() {
        return tFicherosService.findAll();
    }

    @GetMapping("/{codDistribuidora}/{fecCarga}")
    public ResponseEntity<TFicheros> getTFicheroById(
            @PathVariable String codDistribuidora,
            @PathVariable String fecCarga) {
        LocalDateTime localDateTime = LocalDateTime.parse(fecCarga);
        TFicherosId id = new TFicherosId(codDistribuidora, localDateTime);
        Optional<TFicheros> tFichero = tFicherosService.findById(id);
        return tFichero.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TFicheros createTFichero(@RequestBody TFicheros tFicheros) {
        return tFicherosService.save(tFicheros);
    }

    @DeleteMapping("/{codDistribuidora}/{fecCarga}")
    public ResponseEntity<Void> deleteTFichero(
            @PathVariable String codDistribuidora,
            @PathVariable String fecCarga) {
        LocalDateTime localDateTime = LocalDateTime.parse(fecCarga);
        TFicherosId id = new TFicherosId(codDistribuidora, localDateTime);
        tFicherosService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}