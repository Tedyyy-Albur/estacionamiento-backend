package com.estacionamiento.api.strategy;

import com.estacionamiento.api.entity.TipoVehiculo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TarifaResidenteStrategy implements TarifaStrategy {

    private static final BigDecimal TARIFA_POR_MINUTO = new BigDecimal("0.05");

    @Override
    public TipoVehiculo getTipoVehiculo() {
        return TipoVehiculo.RESIDENTE;
    }

    @Override
    public BigDecimal calcularMonto(long minutos) {
        return TARIFA_POR_MINUTO.multiply(BigDecimal.valueOf(minutos))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
