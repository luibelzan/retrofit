package com.celnet.retrofit.controller;

import com.celnet.retrofit.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/kpis/{codDistribuidora}")
    public Map<String, Object> getKpis(@PathVariable String codDistribuidora) {
        Long totalRecepcionados = dashboardService.getTotalRecepcionados(codDistribuidora);
        Long totalRecuperados = dashboardService.getTotalRecuperados(codDistribuidora);
        Long totalAchatarrados = dashboardService.getTotalAchatarrados(codDistribuidora);
        Long totalPendientesEnviar = dashboardService.getTotalPendientesEnviar(codDistribuidora);
        Double tasaRecuperacion = dashboardService.getTasaRecuperacion(codDistribuidora);
        Long contadoresPendientesRecibir = dashboardService.getPendientesRecibir(codDistribuidora);
        Double promedioDias = dashboardService.getPromedioDias(codDistribuidora);

        return Map.of(
                "totalRecepcionados", totalRecepcionados,
                "totalRecuperados", totalRecuperados,
                "totalAchatarrados", totalAchatarrados,
                "totalPendientesEnviar", totalPendientesEnviar,
                "tasaRecuperacion", tasaRecuperacion,
                "contadoresPendientesRecibir", contadoresPendientesRecibir,
                "promedioDias", promedioDias
        );
    }

    @GetMapping("/kpis")
    public List<Map<String, Object>> getAllDistribuidorasKpis() {
        return dashboardService.getAllDistribuidorasWithKpis();
    }
}