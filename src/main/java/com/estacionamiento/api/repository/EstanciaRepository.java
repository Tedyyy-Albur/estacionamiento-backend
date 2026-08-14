package com.estacionamiento.api.repository;

import com.estacionamiento.api.entity.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia, Long> {
    Optional<Estancia> findByPlacaIgnoreCaseAndActivaTrue(String placa);
    List<Estancia> findByPlacaIgnoreCase(String placa);
}
