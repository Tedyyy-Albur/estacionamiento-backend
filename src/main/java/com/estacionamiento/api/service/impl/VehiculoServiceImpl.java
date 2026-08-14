package com.estacionamiento.api.service.impl;

import com.estacionamiento.api.dto.VehiculoResponseDTO;
import com.estacionamiento.api.entity.Residente;
import com.estacionamiento.api.entity.TipoVehiculo;
import com.estacionamiento.api.entity.Vehiculo;
import com.estacionamiento.api.exception.BadRequestException;
import com.estacionamiento.api.repository.EstanciaRepository;
import com.estacionamiento.api.repository.ResidenteRepository;
import com.estacionamiento.api.repository.VehiculoRepository;
import com.estacionamiento.api.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ResidenteRepository residenteRepository;
    private final EstanciaRepository estanciaRepository;

    @Override
    @Transactional
    public Vehiculo registrarVehiculo(String placa, TipoVehiculo tipoVehiculo) {
        String placaNormalizada = placa.trim().toUpperCase();

        Optional<Vehiculo> vehiculoExistente = vehiculoRepository.findByPlacaIgnoreCase(placaNormalizada);
        if (vehiculoExistente.isPresent()) {
            throw new BadRequestException("El vehículo con placa " + placaNormalizada + " ya se encuentra registrado.");
        }

        Vehiculo vehiculo = Vehiculo.builder()
                .placa(placaNormalizada)
                .tipoVehiculo(tipoVehiculo)
                .build();

        if (tipoVehiculo == TipoVehiculo.RESIDENTE) {
            Residente residente = Residente.builder()
                    .placa(placaNormalizada)
                    .tiempoAcumuladoMinutos(0L)
                    .montoAcumulado(BigDecimal.ZERO.setScale(2))
                    .build();
            residenteRepository.save(residente);
        }

        return vehiculoRepository.save(vehiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehiculo obtenerOBuscarVehiculo(String placa) {
        String placaNormalizada = placa.trim().toUpperCase();
        return vehiculoRepository.findByPlacaIgnoreCase(placaNormalizada)
                .orElseGet(() -> Vehiculo.builder()
                        .placa(placaNormalizada)
                        .tipoVehiculo(TipoVehiculo.NO_RESIDENTE)
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> obtenerTodosLosVehiculos() {
        return vehiculoRepository.findAll().stream()
                .map(v -> {
                    boolean estaAdentro = estanciaRepository.findByPlacaIgnoreCaseAndActivaTrue(v.getPlaca()).isPresent();
                    return VehiculoResponseDTO.builder()
                            .placa(v.getPlaca())
                            .tipoVehiculo(v.getTipoVehiculo())
                            .estaAdentro(estaAdentro)
                            .build();
                })
                .toList();
    }
}
