package com.estacionamiento.api.controller;

import com.estacionamiento.api.dto.ApiResponseDTO;
import com.estacionamiento.api.dto.EstanciaEntradaRequestDTO;
import com.estacionamiento.api.dto.EstanciaResponseDTO;
import com.estacionamiento.api.dto.EstanciaSalidaRequestDTO;
import com.estacionamiento.api.service.EstanciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/neo/estancias")
@RequiredArgsConstructor
@Tag(name = "Estancias", description = "Endpoints para registro y consulta de estancias del estacionamiento")
public class EstanciaController {

    private final EstanciaService estanciaService;

    @GetMapping
    @Operation(summary = "Obtener historial de estancias", description = "Retorna la lista de todas las estancias registradas indicando hora de entrada, hora de salida y estado activo/inactivo")
    public ResponseEntity<ApiResponseDTO<List<EstanciaResponseDTO>>> obtenerTodas() {
        List<EstanciaResponseDTO> estancias = estanciaService.obtenerTodasLasEstancias();
        return ResponseEntity.ok(ApiResponseDTO.ok("Historial de estancias obtenido exitosamente", estancias));
    }

    @PostMapping("/entrada")
    @Operation(summary = "Registrar entrada de vehículo", description = "Inicia la estancia de un vehículo en el estacionamiento")
    public ResponseEntity<ApiResponseDTO<EstanciaResponseDTO>> registrarEntrada(@Valid @RequestBody EstanciaEntradaRequestDTO request) {
        EstanciaResponseDTO response = estanciaService.registrarEntrada(request.getPlaca());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Entrada registrada exitosamente", response));
    }

    @PostMapping("/salida")
    @Operation(summary = "Registrar salida de vehículo", description = "Finaliza la estancia, calcula tiempo transcurrido e importe a cobrar")
    public ResponseEntity<ApiResponseDTO<EstanciaResponseDTO>> registrarSalida(@Valid @RequestBody EstanciaSalidaRequestDTO request) {
        EstanciaResponseDTO response = estanciaService.registrarSalida(request.getPlaca());
        return ResponseEntity.ok(ApiResponseDTO.ok("Salida registrada exitosamente", response));
    }
}
