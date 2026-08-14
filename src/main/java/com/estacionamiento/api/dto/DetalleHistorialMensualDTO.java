package com.estacionamiento.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleHistorialMensualDTO {

    private Long id;
    private Integer anio;
    private Integer mes;
    private LocalDateTime fechaCierre;
    private Integer totalEstancias;
    private BigDecimal montoTotalRecaudado;
    private BigDecimal montoTotalResidentes;
    private List<ReporteResidenteDTO> residentes;
}
