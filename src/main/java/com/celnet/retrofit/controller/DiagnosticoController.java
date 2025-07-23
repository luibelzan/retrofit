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
        System.out.println("ID Contador recibido: " + idContador);
        System.out.println("Código Distribuidora recibido: " + codDistribuidora);
        try {
            boolean valido = diagnosticoService.validarContador(idContador, codDistribuidora);
            return ResponseEntity.ok().body(Map.of("valido", valido, "mensaje", "El contador es válido"));
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());  // Log para ver el mensaje de error
            return ResponseEntity.badRequest().body(Map.of("valido", false, "mensaje", ex.getMessage()));
        }
    }

    @PostMapping("/enviar")
    public ResponseEntity<?> actualizarProcesos(
            @RequestBody Map<String, Object> requestData) {  // Usamos Map para recibir los datos

        List<Map<String, Object>> idsRaw = (List<Map<String, Object>>) requestData.get("ids");  // Obtener los datos de 'ids' como una lista de Mapas
        Object codDiagnosticoObj = requestData.get("codDiagnostico");  // Obtener el código de diagnóstico (como Object)

        // Verificar si el código de diagnóstico es un número entero (String o Integer)
        Integer codDiagnostico = null;
        if (codDiagnosticoObj instanceof String) {
            try {
                codDiagnostico = Integer.parseInt((String) codDiagnosticoObj);  // Convertir a entero
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "Código de diagnóstico inválido"));
            }
        } else if (codDiagnosticoObj instanceof Integer) {
            codDiagnostico = (Integer) codDiagnosticoObj;  // Ya es un entero
        }

        if (codDiagnostico == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Código de diagnóstico inválido"));
        }

        // Convertir los mapas a instancias de TProcesosId
        List<TProcesosId> ids = idsRaw.stream()
                .map(idMap -> {
                    TProcesosId tProcesosId = new TProcesosId();
                    tProcesosId.setIdContador((String) idMap.get("idContador"));
                    tProcesosId.setCodDistribuidora((String) idMap.get("codDistribuidora"));
                    return tProcesosId;
                })
                .collect(Collectors.toList());

        // Pasamos los parámetros a la lógica del servicio
        List<TProcesos> procesosActualizados = diagnosticoService.actualizarProcesosMasivos(ids, codDiagnostico);

        // Verificar si la lista de procesos actualizados está vacía
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