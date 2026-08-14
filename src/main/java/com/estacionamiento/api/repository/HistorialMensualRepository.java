package com.estacionamiento.api.repository;

import com.estacionamiento.api.entity.HistorialMensual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialMensualRepository extends JpaRepository<HistorialMensual, Long> {
    List<HistorialMensual> findByAnioAndMes(Integer anio, Integer mes);
    List<HistorialMensual> findByMes(Integer mes);
    List<HistorialMensual> findByAnio(Integer anio);
}
