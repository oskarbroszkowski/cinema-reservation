package com.example.cinema_reservation.service;

import com.example.cinema_reservation.model.CinemaHall;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CinemaHallService {

    List<CinemaHall> findAllCinemaHalls();
    Optional<CinemaHall> findCinemaHallById(Long id);
    Optional<CinemaHall> findCinemaHallByName(String name);
    CinemaHall saveCinemaHall(CinemaHall cinemaHall);
    CinemaHall updateCinemaHall(Long id, CinemaHall updatedCinemaHall);
    void deleteCinemaHallById(Long id);
    int cinemaHallCount();
}
