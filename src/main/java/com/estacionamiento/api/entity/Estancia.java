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
@Table(name = "estancias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String placa;

    @Column(nullable = false)
    private LocalDateTime fechaHoraEntrada;

    private LocalDateTime fechaHoraSalida;

    @Column(nullable = false)
    private Boolean activa;

    private Long minutosTranscurridos;

    private BigDecimal montoCobrado;
}
