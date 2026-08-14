package com.estacionamiento.api.repository;

import com.estacionamiento.api.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {
    Optional<Vehiculo> findByPlacaIgnoreCase(String placa);
}
