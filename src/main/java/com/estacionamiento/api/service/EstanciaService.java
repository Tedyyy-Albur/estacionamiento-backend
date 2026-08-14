package com.estacionamiento.api.service;

import com.estacionamiento.api.dto.DetalleHistorialMensualDTO;
import com.estacionamiento.api.dto.EstanciaResponseDTO;
import com.estacionamiento.api.dto.HistorialMensualDTO;
import com.estacionamiento.api.dto.ReporteResidenteDTO;

import java.util.List;

public interface EstanciaService {
    EstanciaResponseDTO registrarEntrada(String placa);
    EstanciaResponseDTO registrarSalida(String placa);
    List<EstanciaResponseDTO> obtenerTodasLasEstancias();
    List<ReporteResidenteDTO> generarInformePagosResidentes();
    void reiniciarMes();
    List<HistorialMensualDTO> obtenerHistorialesMensuales();
    List<HistorialMensualDTO> buscarHistorialesMensuales(Integer anio, Integer mes);
    DetalleHistorialMensualDTO obtenerDetalleHistorialMensual(Long id);
}
