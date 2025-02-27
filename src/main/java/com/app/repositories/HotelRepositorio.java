package com.app.repositories;

import com.app.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepositorio extends JpaRepository<Hotel, Long> {


    Hotel findByCodigoHotel(String codigoHotel);

    Hotel findTopByOrderByCodigoHotelDesc();
}
