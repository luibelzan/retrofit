package com.celnet.retrofit.controller;

import com.celnet.retrofit.model.TDiagnostico;
import com.celnet.retrofit.model.TDistribuidoras;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.id.TProcesosId;
import com.celnet.retrofit.repository.TDiagnosticoRepository;
import com.celnet.retrofit.repository.TDistribuidorasRepository;
import com.celnet.retrofit.service.DiagnosticoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/achatarrado")
public class DiagnosticoController {

    private final TDistribuidorasRepository distribuidoraRepository;
    private final TDiagnosticoRepository diagnosticoRepository;
    private final DiagnosticoService diagnosticoService;

    public DiagnosticoController(TDistribuidorasRepository distribuidoraRepository, TDiagnosticoRepository diagnosticoRepository, DiagnosticoService diagnosticoService) {
        this.distribuidoraRepository = distribuidoraRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.diagnosticoService = diagnosticoService;
    }

    @GetMapping("/validar/{idContador}")
    public ResponseEntity<?> validarContador(
            @PathVariable String idContador,
            @RequestParam String codDistribuidora
    ) {
        try {
            TProcesos proceso = diagnosticoService.validarContador(idContador, codDistribuidora);
            String desAlmacen = diagnosticoService.getDesAlmacenByCodAlmacen(proceso.getCodAlmacen(), proceso.getCodDistribuidora());
            return ResponseEntity.ok().body(Map.of(
                "valido", true,
                "mensaje", "El contador es válido",
                "idContador", proceso.getIdContador(),
                "codDistribuidora", proceso.getCodDistribuidora(),
                "codAlmacen", proceso.getCodAlmacen(),
                "desAlmacen", desAlmacen,
                "fecRecepcion", proceso.getFecRecepcion(),
                "fecRecepcion2", proceso.getFecRecepcion2()
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                "valido", false,
                "mensaje", ex.getMessage()
            ));
        }
    }


    @PostMapping("/enviar")
    public ResponseEntity<?> actualizarProcesos(@RequestBody Map<String,Object> requestData) {
        List<Map<String, Object>> contadores = (List<Map<String, Object>>) requestData.get("ids");
        String tipDiagnostico = (String) requestData.get("tipDiagnostico");

        List<TProcesos> procesosActualizados = new ArrayList<>();
        for (Map<String, Object> c : contadores) {
            String idContador = (String) c.get("idContador");
            String codDistribuidora = (String) c.get("codDistribuidora");
            Integer codDiagnostico = Integer.parseInt(c.get("codDiagnostico").toString());

            procesosActualizados.add(
                diagnosticoService.actualizarProceso(idContador, codDistribuidora, codDiagnostico, tipDiagnostico)
            );
        }

        if (procesosActualizados.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "No se actualizaron procesos"));
        }
        return ResponseEntity.ok(procesosActualizados);
    }


    @GetMapping("/distribuidoras")
    public List<TDistribuidoras> obtenerDistribuidoras() {
        return distribuidoraRepository.findAllDistribuidoras();
    }

    @GetMapping("/diagnosticosCH")
    public List<TDiagnostico> obtenerDiagnosticosCH() {
        return diagnosticoRepository.findByTipDiagnostico("CH");
    }

    @GetMapping("/diagnosticosRP")
    public List<TDiagnostico> obtenerDiagnosticosRP() {
        return diagnosticoRepository.findByTipDiagnostico("RP");
    }
}