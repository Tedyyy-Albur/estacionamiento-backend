package com.estacionamiento.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstanciaSalidaRequestDTO {

    @NotBlank(message = "La placa es obligatoria para registrar la salida")
    private String placa;
}
