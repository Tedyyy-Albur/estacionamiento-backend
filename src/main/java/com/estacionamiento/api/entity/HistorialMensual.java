package com.estacionamiento.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_mensual")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialMensual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private LocalDateTime fechaCierre;

    @Column(nullable = false)
    private Integer totalEstancias;

    @Column(nullable = false)
    private BigDecimal montoTotalRecaudado;

    @Column(nullable = false)
    private BigDecimal montoTotalResidentes;
}
