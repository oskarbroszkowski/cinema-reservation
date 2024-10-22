package com.example.cinema_reservation.repository;

import com.example.cinema_reservation.model.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaHallRepository extends JpaRepository<CinemaHall, Long> {
    List<CinemaHall> findByHallType(String hallType);
    long count();
}
