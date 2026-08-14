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

@Entity
@Table(name = "historial_residente_mes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialResidenteMes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long historialMensualId;

    @Column(nullable = false)
    private String placa;

    @Column(nullable = false)
    private Long tiempoAcumuladoMinutos;

    @Column(nullable = false)
    private BigDecimal montoTotal;
}
