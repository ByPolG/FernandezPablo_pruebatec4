package com.app.repositories;

import com.app.entities.ReservaVuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaVueloRepositorio extends JpaRepository<ReservaVuelo, Long> {

}
