package com.estacionamiento.api.service.impl;

import com.estacionamiento.api.dto.DetalleHistorialMensualDTO;
import com.estacionamiento.api.dto.EstanciaResponseDTO;
import com.estacionamiento.api.dto.HistorialMensualDTO;
import com.estacionamiento.api.dto.ReporteResidenteDTO;
import com.estacionamiento.api.entity.Estancia;
import com.estacionamiento.api.entity.HistorialMensual;
import com.estacionamiento.api.entity.HistorialResidenteMes;
import com.estacionamiento.api.entity.Residente;
import com.estacionamiento.api.entity.TipoVehiculo;
import com.estacionamiento.api.entity.Vehiculo;
import com.estacionamiento.api.exception.BadRequestException;
import com.estacionamiento.api.exception.ResourceNotFoundException;
import com.estacionamiento.api.repository.EstanciaRepository;
import com.estacionamiento.api.repository.HistorialMensualRepository;
import com.estacionamiento.api.repository.HistorialResidenteMesRepository;
import com.estacionamiento.api.repository.ResidenteRepository;
import com.estacionamiento.api.service.EstanciaService;
import com.estacionamiento.api.service.VehiculoService;
import com.estacionamiento.api.strategy.TarifaStrategy;
import com.estacionamiento.api.strategy.TarifaStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstanciaServiceImpl implements EstanciaService {

    private final EstanciaRepository estanciaRepository;
    private final ResidenteRepository residenteRepository;
    private final HistorialMensualRepository historialMensualRepository;
    private final HistorialResidenteMesRepository historialResidenteMesRepository;
    private final VehiculoService vehiculoService;
    private final TarifaStrategyFactory tarifaStrategyFactory;

    @Override
    @Transactional
    public EstanciaResponseDTO registrarEntrada(String placa) {
        String placaNormalizada = placa.trim().toUpperCase();

        estanciaRepository.findByPlacaIgnoreCaseAndActivaTrue(placaNormalizada)
                .ifPresent(e -> {
                    throw new BadRequestException("El vehículo con placa " + placaNormalizada + " ya se encuentra dentro del estacionamiento.");
                });

        Vehiculo vehiculo = vehiculoService.obtenerOBuscarVehiculo(placaNormalizada);

        Estancia estancia = Estancia.builder()
                .placa(placaNormalizada)
                .fechaHoraEntrada(LocalDateTime.now())
                .activa(true)
                .build();

        Estancia guardada = estanciaRepository.save(estancia);

        return EstanciaResponseDTO.builder()
                .id(guardada.getId())
                .placa(guardada.getPlaca())
                .tipoVehiculo(vehiculo.getTipoVehiculo())
                .fechaHoraEntrada(guardada.getFechaHoraEntrada())
                .activa(true)
                .build();
    }

    @Override
    @Transactional
    public EstanciaResponseDTO registrarSalida(String placa) {
        String placaNormalizada = placa.trim().toUpperCase();

        Estancia estancia = estanciaRepository.findByPlacaIgnoreCaseAndActivaTrue(placaNormalizada)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró estancia activa para el vehículo con placa: " + placaNormalizada));

        Vehiculo vehiculo = vehiculoService.obtenerOBuscarVehiculo(placaNormalizada);
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = Duration.between(estancia.getFechaHoraEntrada(), ahora).toMinutes();
        if (minutos < 0) {
            minutos = 0;
        }

        TarifaStrategy strategy = tarifaStrategyFactory.getStrategy(vehiculo.getTipoVehiculo());
        BigDecimal montoCalculado = strategy.calcularMonto(minutos);

        estancia.setFechaHoraSalida(ahora);
        estancia.setMinutosTranscurridos(minutos);
        estancia.setMontoCobrado(montoCalculado);
        estancia.setActiva(false);

        if (vehiculo.getTipoVehiculo() == TipoVehiculo.RESIDENTE) {
            Residente residente = residenteRepository.findByPlacaIgnoreCase(placaNormalizada)
                    .orElseGet(() -> Residente.builder()
                            .placa(placaNormalizada)
                            .tiempoAcumuladoMinutos(0L)
                            .montoAcumulado(BigDecimal.ZERO.setScale(2))
                            .build());

            residente.setTiempoAcumuladoMinutos(residente.getTiempoAcumuladoMinutos() + minutos);
            residente.setMontoAcumulado(residente.getMontoAcumulado().add(montoCalculado));
            residenteRepository.save(residente);
        }

        Estancia actualizada = estanciaRepository.save(estancia);

        return EstanciaResponseDTO.builder()
                .id(actualizada.getId())
                .placa(actualizada.getPlaca())
                .tipoVehiculo(vehiculo.getTipoVehiculo())
                .fechaHoraEntrada(actualizada.getFechaHoraEntrada())
                .fechaHoraSalida(actualizada.getFechaHoraSalida())
                .minutosTranscurridos(minutos)
                .montoCobrado(montoCalculado)
                .activa(false)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstanciaResponseDTO> obtenerTodasLasEstancias() {
        return estanciaRepository.findAll().stream()
                .map(e -> {
                    Vehiculo v = vehiculoService.obtenerOBuscarVehiculo(e.getPlaca());
                    return EstanciaResponseDTO.builder()
                            .id(e.getId())
                            .placa(e.getPlaca())
                            .tipoVehiculo(v.getTipoVehiculo())
                            .fechaHoraEntrada(e.getFechaHoraEntrada())
                            .fechaHoraSalida(e.getFechaHoraSalida())
                            .minutosTranscurridos(e.getMinutosTranscurridos())
                            .montoCobrado(e.getMontoCobrado())
                            .activa(e.getActiva())
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResidenteDTO> generarInformePagosResidentes() {
        return residenteRepository.findAll().stream()
                .map(r -> ReporteResidenteDTO.builder()
                        .placa(r.getPlaca())
                        .tiempoAcumuladoMinutos(r.getTiempoAcumuladoMinutos())
                        .montoTotalPagar(r.getMontoAcumulado())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void reiniciarMes() {
        LocalDateTime ahora = LocalDateTime.now();
        int anioActual = ahora.getYear();
        int mesActual = ahora.getMonthValue();

        List<Estancia> estancias = estanciaRepository.findAll();
        List<Residente> residentes = residenteRepository.findAll();

        int totalEstanciasActuales = estancias.size();

        BigDecimal montoTotalRecaudadoActual = estancias.stream()
                .map(Estancia::getMontoCobrado)
                .filter(m -> m != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2);

        BigDecimal montoTotalResidentesActual = residentes.stream()
                .map(Residente::getMontoAcumulado)
                .filter(m -> m != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2);

        // Buscar si ya existe un respaldo para el mismo año y mes
        List<HistorialMensual> existentes = historialMensualRepository.findByAnioAndMes(anioActual, mesActual);
        HistorialMensual historialGuardado;

        if (!existentes.isEmpty()) {
            // Si ya existe respaldo en este mismo mes, acumulamos los datos
            historialGuardado = existentes.get(0);
            historialGuardado.setFechaCierre(ahora);
            historialGuardado.setTotalEstancias(historialGuardado.getTotalEstancias() + totalEstanciasActuales);
            historialGuardado.setMontoTotalRecaudado(historialGuardado.getMontoTotalRecaudado().add(montoTotalRecaudadoActual));
            historialGuardado.setMontoTotalResidentes(historialGuardado.getMontoTotalResidentes().add(montoTotalResidentesActual));
            historialGuardado = historialMensualRepository.save(historialGuardado);
        } else {
            // Si es un mes nuevo, creamos nuevo registro de respaldo
            HistorialMensual nuevoHistorial = HistorialMensual.builder()
                    .anio(anioActual)
                    .mes(mesActual)
                    .fechaCierre(ahora)
                    .totalEstancias(totalEstanciasActuales)
                    .montoTotalRecaudado(montoTotalRecaudadoActual)
                    .montoTotalResidentes(montoTotalResidentesActual)
                    .build();
            historialGuardado = historialMensualRepository.save(nuevoHistorial);
        }

        // Guardar o acumular el desglose de residentes
        for (Residente r : residentes) {
            final Long hId = historialGuardado.getId();
            Optional<HistorialResidenteMes> residenteMesOpt = historialResidenteMesRepository
                    .findByHistorialMensualIdAndPlacaIgnoreCase(hId, r.getPlaca());

            if (residenteMesOpt.isPresent()) {
                HistorialResidenteMes hrm = residenteMesOpt.get();
                hrm.setTiempoAcumuladoMinutos(hrm.getTiempoAcumuladoMinutos() + r.getTiempoAcumuladoMinutos());
                hrm.setMontoTotal(hrm.getMontoTotal().add(r.getMontoAcumulado()));
                historialResidenteMesRepository.save(hrm);
            } else {
                HistorialResidenteMes hrm = HistorialResidenteMes.builder()
                        .historialMensualId(hId)
                        .placa(r.getPlaca())
                        .tiempoAcumuladoMinutos(r.getTiempoAcumuladoMinutos())
                        .montoTotal(r.getMontoAcumulado())
                        .build();
                historialResidenteMesRepository.save(hrm);
            }

            // Resetear acumulados del residente para el nuevo ciclo
            r.setTiempoAcumuladoMinutos(0L);
            r.setMontoAcumulado(BigDecimal.ZERO.setScale(2));
        }
        residenteRepository.saveAll(residentes);

        // Limpiar estancias del ciclo
        estanciaRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMensualDTO> obtenerHistorialesMensuales() {
        return historialMensualRepository.findAll().stream()
                .map(h -> HistorialMensualDTO.builder()
                        .id(h.getId())
                        .anio(h.getAnio())
                        .mes(h.getMes())
                        .fechaCierre(h.getFechaCierre())
                        .totalEstancias(h.getTotalEstancias())
                        .montoTotalRecaudado(h.getMontoTotalRecaudado())
                        .montoTotalResidentes(h.getMontoTotalResidentes())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMensualDTO> buscarHistorialesMensuales(Integer anio, Integer mes) {
        List<HistorialMensual> lista;
        if (anio != null && mes != null) {
            lista = historialMensualRepository.findByAnioAndMes(anio, mes);
        } else if (mes != null) {
            lista = historialMensualRepository.findByMes(mes);
        } else if (anio != null) {
            lista = historialMensualRepository.findByAnio(anio);
        } else {
            lista = historialMensualRepository.findAll();
        }

        return lista.stream()
                .map(h -> HistorialMensualDTO.builder()
                        .id(h.getId())
                        .anio(h.getAnio())
                        .mes(h.getMes())
                        .fechaCierre(h.getFechaCierre())
                        .totalEstancias(h.getTotalEstancias())
                        .montoTotalRecaudado(h.getMontoTotalRecaudado())
                        .montoTotalResidentes(h.getMontoTotalResidentes())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleHistorialMensualDTO obtenerDetalleHistorialMensual(Long id) {
        HistorialMensual h = historialMensualRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró respaldo mensual con ID: " + id));

        List<ReporteResidenteDTO> residentes = historialResidenteMesRepository.findByHistorialMensualId(id).stream()
                .map(r -> ReporteResidenteDTO.builder()
                        .placa(r.getPlaca())
                        .tiempoAcumuladoMinutos(r.getTiempoAcumuladoMinutos())
                        .montoTotalPagar(r.getMontoTotal())
                        .build())
                .toList();

        return DetalleHistorialMensualDTO.builder()
                .id(h.getId())
                .anio(h.getAnio())
                .mes(h.getMes())
                .fechaCierre(h.getFechaCierre())
                .totalEstancias(h.getTotalEstancias())
                .montoTotalRecaudado(h.getMontoTotalRecaudado())
                .montoTotalResidentes(h.getMontoTotalResidentes())
                .residentes(residentes)
                .build();
    }
}
