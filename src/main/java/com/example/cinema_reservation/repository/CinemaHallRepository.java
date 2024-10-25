package com.example.cinema_reservation.repository;

import com.example.cinema_reservation.model.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaHallRepository extends JpaRepository<CinemaHall, Long> {
    List<CinemaHall> findByHallType(String hallType);
    Optional<CinemaHall> findByName(String name);
    long count();
}
