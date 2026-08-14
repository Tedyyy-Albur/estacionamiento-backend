package com.estacionamiento.api.controller;

import com.estacionamiento.api.dto.ApiResponseDTO;
import com.estacionamiento.api.dto.ReporteResidenteDTO;
import com.estacionamiento.api.service.EstanciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/neo/residentes")
@RequiredArgsConstructor
@Tag(name = "Residentes", description = "Endpoints de reportes e información de vehículos residentes")
public class ResidenteController {

    private final EstanciaService estanciaService;

    @GetMapping("/pagos")
    @Operation(summary = "Generar informe de pagos de residentes", description = "Obtiene la lista de residentes con sus minutos acumulados y monto total a pagar")
    public ResponseEntity<ApiResponseDTO<List<ReporteResidenteDTO>>> generarInformePagos() {
        List<ReporteResidenteDTO> reporte = estanciaService.generarInformePagosResidentes();
        return ResponseEntity.ok(ApiResponseDTO.ok("Informe de pagos generado exitosamente", reporte));
    }
}
