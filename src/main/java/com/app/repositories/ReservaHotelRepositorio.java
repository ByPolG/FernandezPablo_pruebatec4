package com.app.repositories;

import com.app.entities.ReservaHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaHotelRepositorio extends JpaRepository<ReservaHotel, Long> {

}
