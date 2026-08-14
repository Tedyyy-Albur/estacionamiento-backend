package com.estacionamiento.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "residentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Residente {

    @Id
    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false)
    private Long tiempoAcumuladoMinutos;

    @Column(nullable = false)
    private BigDecimal montoAcumulado;
}
