package com.estacionamiento.api.service;

import com.estacionamiento.api.dto.VehiculoResponseDTO;
import com.estacionamiento.api.entity.TipoVehiculo;
import com.estacionamiento.api.entity.Vehiculo;

import java.util.List;

public interface VehiculoService {
    Vehiculo registrarVehiculo(String placa, TipoVehiculo tipoVehiculo);
    Vehiculo obtenerOBuscarVehiculo(String placa);
    List<VehiculoResponseDTO> obtenerTodosLosVehiculos();
}
