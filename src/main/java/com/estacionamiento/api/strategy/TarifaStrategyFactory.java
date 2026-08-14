package com.estacionamiento.api.strategy;

import com.estacionamiento.api.entity.TipoVehiculo;
import com.estacionamiento.api.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TarifaStrategyFactory {

    private final Map<TipoVehiculo, TarifaStrategy> strategies;

    public TarifaStrategyFactory(List<TarifaStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(TarifaStrategy::getTipoVehiculo, Function.identity()));
    }

    public TarifaStrategy getStrategy(TipoVehiculo tipoVehiculo) {
        TarifaStrategy strategy = strategies.get(tipoVehiculo);
        if (strategy == null) {
            throw new BadRequestException("No existe una estrategia de tarifa configurada para el tipo: " + tipoVehiculo);
        }
        return strategy;
    }
}
