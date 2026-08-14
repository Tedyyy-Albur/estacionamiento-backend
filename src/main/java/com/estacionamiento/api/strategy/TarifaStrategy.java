package com.estacionamiento.api.strategy;

import com.estacionamiento.api.entity.TipoVehiculo;
import java.math.BigDecimal;

public interface TarifaStrategy {
    TipoVehiculo getTipoVehiculo();
    BigDecimal calcularMonto(long minutos);
}
