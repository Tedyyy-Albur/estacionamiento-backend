package com.estacionamiento.api.dto;

import com.estacionamiento.api.entity.TipoVehiculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoResponseDTO {

    private String placa;
    private TipoVehiculo tipoVehiculo;
    private Boolean estaAdentro;
}
