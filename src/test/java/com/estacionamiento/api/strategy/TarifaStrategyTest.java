package com.estacionamiento.api.strategy;

import com.estacionamiento.api.entity.TipoVehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TarifaStrategyTest {

    private TarifaStrategyFactory factory;

    @BeforeEach
    void setUp() {
        List<TarifaStrategy> strategies = List.of(
                new TarifaOficialStrategy(),
                new TarifaResidenteStrategy(),
                new TarifaNoResidenteStrategy()
        );
        factory = new TarifaStrategyFactory(strategies);
    }

    @Test
    @DisplayName("Vehículo oficial siempre debe pagar $0.00")
    void testTarifaOficial() {
        TarifaStrategy strategy = factory.getStrategy(TipoVehiculo.OFICIAL);
        BigDecimal monto = strategy.calcularMonto(120); // 2 horas
        assertEquals(new BigDecimal("0.00"), monto);
    }

    @Test
    @DisplayName("Vehículo residente paga $0.05 por minuto")
    void testTarifaResidente() {
        TarifaStrategy strategy = factory.getStrategy(TipoVehiculo.RESIDENTE);
        BigDecimal monto = strategy.calcularMonto(100); // 100 min * 0.05 = 5.00
        assertEquals(new BigDecimal("5.00"), monto);
    }

    @Test
    @DisplayName("Vehículo no residente paga $0.50 por minuto")
    void testTarifaNoResidente() {
        TarifaStrategy strategy = factory.getStrategy(TipoVehiculo.NO_RESIDENTE);
        BigDecimal monto = strategy.calcularMonto(60); // 60 min * 0.50 = 30.00
        assertEquals(new BigDecimal("30.00"), monto);
    }
}
