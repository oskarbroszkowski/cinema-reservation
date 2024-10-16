package com.example.cinema_reservation.repository;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByMovie(Movie movie);
}