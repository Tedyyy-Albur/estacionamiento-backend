package com.estacionamiento.api.dto;

import com.estacionamiento.api.entity.TipoVehiculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstanciaResponseDTO {

    private Long id;
    private String placa;
    private TipoVehiculo tipoVehiculo;
    private LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;
    private Long minutosTranscurridos;
    private BigDecimal montoCobrado;
    private Boolean activa;
}
