package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TDiagnostico;
import com.celnet.retrofit.model.id.TDiagnosticoId;
import com.celnet.retrofit.service.TDiagnosticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/diagnosticos")
public class TDiagnosticoController {

    @Autowired
    private TDiagnosticoService tDiagnosticoService;

    @GetMapping
    public List<TDiagnostico> getAllDiagnosticos() {
        return tDiagnosticoService.findAll();
    }

    @GetMapping("/{tipDiagnostico}/{codDiagnostico}")
    public ResponseEntity<TDiagnostico> getDiagnosticoById(
            @PathVariable String tipDiagnostico,
            @PathVariable Integer codDiagnostico) {
        TDiagnosticoId id = new TDiagnosticoId(tipDiagnostico, codDiagnostico);
        return tDiagnosticoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TDiagnostico createDiagnostico(@RequestBody TDiagnostico diagnostico) {
        return tDiagnosticoService.save(diagnostico);
    }

    @PutMapping
    public ResponseEntity<TDiagnostico> updateDiagnostico(@RequestBody TDiagnostico diagnostico) {
        try {
            return ResponseEntity.ok(tDiagnosticoService.update(diagnostico));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{tipDiagnostico}/{codDiagnostico}")
    public ResponseEntity<Void> deleteDiagnostico(
            @PathVariable String tipDiagnostico,
            @PathVariable Integer codDiagnostico) {
        TDiagnosticoId id = new TDiagnosticoId(tipDiagnostico, codDiagnostico);
        if (tDiagnosticoService.findById(id).isPresent()) {
            tDiagnosticoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
