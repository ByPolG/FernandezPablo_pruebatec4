package com.app.repositories;

import com.app.entities.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitacionRepositorio extends JpaRepository<Habitacion, Long> {

}
