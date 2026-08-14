package com.estacionamiento.api.controller;

import com.estacionamiento.api.dto.ApiResponseDTO;
import com.estacionamiento.api.dto.DetalleHistorialMensualDTO;
import com.estacionamiento.api.dto.HistorialMensualDTO;
import com.estacionamiento.api.service.EstanciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/neo/mes")
@RequiredArgsConstructor
@Tag(name = "Administración Mensual", description = "Operaciones de reinicio, respaldos e historial de ciclos mensuales")
public class MesController {

    private final EstanciaService estanciaService;

    @PostMapping("/iniciar")
    @Operation(summary = "Reiniciar mes", description = "Genera un respaldo automático en base de datos del mes actual y posteriormente resetea acumulados y estancias")
    public ResponseEntity<ApiResponseDTO<Void>> reiniciarMes() {
        estanciaService.reiniciarMes();
        return ResponseEntity.ok(ApiResponseDTO.ok("El mes ha sido respaldado y reiniciado exitosamente"));
    }

    @GetMapping("/historial/buscar/{anio}/{mes}")
    @Operation(summary = "Buscar respaldo mensual por año y mes", description = "Filtra los respaldos mensuales por año y mes especificados en la ruta")
    public ResponseEntity<ApiResponseDTO<List<HistorialMensualDTO>>> buscarPorAnioYMes(
            @PathVariable Integer anio,
            @PathVariable Integer mes) {
        List<HistorialMensualDTO> historiales = estanciaService.buscarHistorialesMensuales(anio, mes);
        return ResponseEntity.ok(ApiResponseDTO.ok("Respaldos encontrados para el período " + mes + "/" + anio, historiales));
    }

    @GetMapping("/historial/{id}")
    @Operation(summary = "Obtener detalle de un cierre mensual respaldado por ID", description = "Retorna el desglose completo del reporte de residentes y totales de un mes respaldado")
    public ResponseEntity<ApiResponseDTO<DetalleHistorialMensualDTO>> obtenerDetalleHistorial(@PathVariable Long id) {
        DetalleHistorialMensualDTO detalle = estanciaService.obtenerDetalleHistorialMensual(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Detalle del cierre mensual obtenido exitosamente", detalle));
    }
}
