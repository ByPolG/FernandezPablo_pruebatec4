package com.app.repositories;

import com.app.entities.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VueloRepositorio extends JpaRepository<Vuelo, Long> {
    Vuelo findByCodigoVuelo(String codigoVuelo);

    Optional<Vuelo> findByOrigenAndDestinoAndFechaIda(String origen, String destino, LocalDate fechaIda);

    // Esta consulta es suficiente, no se requiere SQL explícito
    Vuelo findTopByOrderByCodigoVueloDesc(); // Esto devuelve la entidad completa con el código de vuelo más alto
}

