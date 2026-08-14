package com.estacionamiento.api.repository;

import com.estacionamiento.api.entity.Residente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResidenteRepository extends JpaRepository<Residente, String> {
    Optional<Residente> findByPlacaIgnoreCase(String placa);
}
