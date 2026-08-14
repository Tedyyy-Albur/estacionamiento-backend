package com.estacionamiento.api.strategy;

import com.estacionamiento.api.entity.TipoVehiculo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TarifaOficialStrategy implements TarifaStrategy {

    @Override
    public TipoVehiculo getTipoVehiculo() {
        return TipoVehiculo.OFICIAL;
    }

    @Override
    public BigDecimal calcularMonto(long minutos) {
        return BigDecimal.ZERO.setScale(2);
    }
}
