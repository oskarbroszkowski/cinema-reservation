package com.example.cinema_reservation.service;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.model.Screening;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ScreeningService {

    List<Screening> findAllScreenings();
    Optional<Screening> findScreeningById(Long id);
    Screening saveScreening(Screening screening);
    void deleteScreeningById(Long id);
    List<Screening> findScreeningByMovie(Movie movie);
    int screeningsCount();
}

