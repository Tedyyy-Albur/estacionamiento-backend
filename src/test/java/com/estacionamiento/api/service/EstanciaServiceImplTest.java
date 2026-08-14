package com.estacionamiento.api.service;

import com.estacionamiento.api.dto.EstanciaResponseDTO;
import com.estacionamiento.api.entity.Estancia;
import com.estacionamiento.api.entity.TipoVehiculo;
import com.estacionamiento.api.entity.Vehiculo;
import com.estacionamiento.api.exception.BadRequestException;
import com.estacionamiento.api.exception.ResourceNotFoundException;
import com.estacionamiento.api.repository.EstanciaRepository;
import com.estacionamiento.api.repository.ResidenteRepository;
import com.estacionamiento.api.service.impl.EstanciaServiceImpl;
import com.estacionamiento.api.strategy.TarifaNoResidenteStrategy;
import com.estacionamiento.api.strategy.TarifaOficialStrategy;
import com.estacionamiento.api.strategy.TarifaResidenteStrategy;
import com.estacionamiento.api.strategy.TarifaStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstanciaServiceImplTest {

    @Mock
    private EstanciaRepository estanciaRepository;

    @Mock
    private ResidenteRepository residenteRepository;

    @Mock
    private VehiculoService vehiculoService;

    @Spy
    private TarifaStrategyFactory tarifaStrategyFactory = new TarifaStrategyFactory(
            List.of(new TarifaOficialStrategy(), new TarifaResidenteStrategy(), new TarifaNoResidenteStrategy())
    );

    @InjectMocks
    private EstanciaServiceImpl estanciaService;

    private String placa;

    @BeforeEach
    void setUp() {
        placa = "ABC-1234";
    }

    @Test
    @DisplayName("Registrar entrada exitosa")
    void testRegistrarEntradaExito() {
        when(estanciaRepository.findByPlacaIgnoreCaseAndActivaTrue(placa)).thenReturn(Optional.empty());
        when(vehiculoService.obtenerOBuscarVehiculo(placa)).thenReturn(Vehiculo.builder()
                .placa(placa)
                .tipoVehiculo(TipoVehiculo.NO_RESIDENTE)
                .build());

        Estancia estanciaGuardada = Estancia.builder()
                .id(1L)
                .placa(placa)
                .fechaHoraEntrada(LocalDateTime.now())
                .activa(true)
                .build();

        when(estanciaRepository.save(any(Estancia.class))).thenReturn(estanciaGuardada);

        EstanciaResponseDTO response = estanciaService.registrarEntrada(placa);

        assertNotNull(response);
        assertEquals(placa, response.getPlaca());
        assertTrue(response.getActiva());
        assertEquals(TipoVehiculo.NO_RESIDENTE, response.getTipoVehiculo());
        verify(estanciaRepository, times(1)).save(any(Estancia.class));
    }

    @Test
    @DisplayName("Lanza excepción si vehículo ya tiene entrada activa")
    void testRegistrarEntradaDuplicada() {
        Estancia estanciaActiva = Estancia.builder()
                .id(1L)
                .placa(placa)
                .activa(true)
                .build();

        when(estanciaRepository.findByPlacaIgnoreCaseAndActivaTrue(placa)).thenReturn(Optional.of(estanciaActiva));

        assertThrows(BadRequestException.class, () -> estanciaService.registrarEntrada(placa));
    }

    @Test
    @DisplayName("Registrar salida calcula importe correctamente para No Residente")
    void testRegistrarSalidaNoResidente() {
        LocalDateTime entrada = LocalDateTime.now().minusMinutes(60);
        Estancia estanciaActiva = Estancia.builder()
                .id(1L)
                .placa(placa)
                .fechaHoraEntrada(entrada)
                .activa(true)
                .build();

        when(estanciaRepository.findByPlacaIgnoreCaseAndActivaTrue(placa)).thenReturn(Optional.of(estanciaActiva));
        when(vehiculoService.obtenerOBuscarVehiculo(placa)).thenReturn(Vehiculo.builder()
                .placa(placa)
                .tipoVehiculo(TipoVehiculo.NO_RESIDENTE)
                .build());

        when(estanciaRepository.save(any(Estancia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EstanciaResponseDTO response = estanciaService.registrarSalida(placa);

        assertNotNull(response);
        assertFalse(response.getActiva());
        assertEquals(60L, response.getMinutosTranscurridos());
        assertEquals(new BigDecimal("30.00"), response.getMontoCobrado()); // 60 * 0.50
    }

    @Test
    @DisplayName("Lanza excepción si no se encuentra estancia activa al registrar salida")
    void testRegistrarSalidaSinEstancia() {
        when(estanciaRepository.findByPlacaIgnoreCaseAndActivaTrue(placa)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> estanciaService.registrarSalida(placa));
    }
}
