package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TGeneral;
import com.celnet.retrofit.model.id.TGeneralId;
import com.celnet.retrofit.service.TGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/general")
public class TGeneralController {

    private final TGeneralService tGeneralService;

    @Autowired
    public TGeneralController(TGeneralService tGeneralService) {
        this.tGeneralService = tGeneralService;
    }

    @GetMapping
    public ResponseEntity<List<TGeneral>> getAllTGenerals() {
        List<TGeneral> result = tGeneralService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{codDistribuidora}/{idContador}")
    public ResponseEntity<TGeneral> getTGeneralById(@PathVariable String codDistribuidora, @PathVariable String idContador) {
        TGeneralId id = new TGeneralId(codDistribuidora, idContador);
        Optional<TGeneral> tGeneral = tGeneralService.findById(id);
        return tGeneral.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<TGeneral> saveTGeneral(@RequestBody TGeneral tGeneral) {
        TGeneral savedTGeneral = tGeneralService.save(tGeneral);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTGeneral);
    }

    @PutMapping("/{codDistribuidora}/{idContador}")
    public ResponseEntity<TGeneral> updateTGeneral(@PathVariable String codDistribuidora, @PathVariable String idContador, @RequestBody TGeneral tGeneral) {
        TGeneralId id = new TGeneralId(codDistribuidora, idContador);
        if (!tGeneralService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        tGeneral.setCodDistribuidora(codDistribuidora);
        tGeneral.setIdContador(idContador);
        TGeneral updatedTGeneral = tGeneralService.save(tGeneral);
        return ResponseEntity.ok(updatedTGeneral);
    }

    @DeleteMapping("/{codDistribuidora}/{idContador}")
    public ResponseEntity<Void> deleteByCodGeneral(@PathVariable String codDistribuidora, @PathVariable String idContador) {
        TGeneralId id = new TGeneralId(codDistribuidora, idContador);
        if (!tGeneralService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        tGeneralService.deleteByCodGeneral(codDistribuidora, idContador);
        return ResponseEntity.noContent().build();
    }
}