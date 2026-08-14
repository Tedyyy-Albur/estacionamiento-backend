package com.estacionamiento.api.controller;

import com.estacionamiento.api.dto.ApiResponseDTO;
import com.estacionamiento.api.dto.VehiculoRequestDTO;
import com.estacionamiento.api.dto.VehiculoResponseDTO;
import com.estacionamiento.api.entity.TipoVehiculo;
import com.estacionamiento.api.entity.Vehiculo;
import com.estacionamiento.api.service.VehiculoService;
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
@RequestMapping("/neo/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehículos", description = "Endpoints para registro y gestión de vehículos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    @Operation(summary = "Obtener listado de vehículos", description = "Retorna la lista de todos los vehículos registrados indicando si están dentro del estacionamiento")
    public ResponseEntity<ApiResponseDTO<List<VehiculoResponseDTO>>> obtenerTodos() {
        List<VehiculoResponseDTO> vehiculos = vehiculoService.obtenerTodosLosVehiculos();
        return ResponseEntity.ok(ApiResponseDTO.ok("Listado de vehículos obtenido exitosamente", vehiculos));
    }

    @PostMapping("/oficiales")
    @Operation(summary = "Alta de vehículo oficial", description = "Registra un vehículo oficial que no paga tarifa de estacionamiento")
    public ResponseEntity<ApiResponseDTO<Vehiculo>> registrarOficial(@Valid @RequestBody VehiculoRequestDTO request) {
        Vehiculo v = vehiculoService.registrarVehiculo(request.getPlaca(), TipoVehiculo.OFICIAL);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Vehículo oficial registrado exitosamente", v));
    }

    @PostMapping("/residentes")
    @Operation(summary = "Alta de vehículo residente", description = "Registra un vehículo residente con acumulado de $0.05/minuto")
    public ResponseEntity<ApiResponseDTO<Vehiculo>> registrarResidente(@Valid @RequestBody VehiculoRequestDTO request) {
        Vehiculo v = vehiculoService.registrarVehiculo(request.getPlaca(), TipoVehiculo.RESIDENTE);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Vehículo residente registrado exitosamente", v));
    }

    @PostMapping("/no-residentes")
    @Operation(summary = "Alta de vehículo no residente", description = "Registra explícitamente un vehículo no residente")
    public ResponseEntity<ApiResponseDTO<Vehiculo>> registrarNoResidente(@Valid @RequestBody VehiculoRequestDTO request) {
        Vehiculo v = vehiculoService.registrarVehiculo(request.getPlaca(), TipoVehiculo.NO_RESIDENTE);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Vehículo no residente registrado exitosamente", v));
    }
}
