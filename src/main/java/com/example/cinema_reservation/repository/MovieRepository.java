package com.example.cinema_reservation.repository;

import com.example.cinema_reservation.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    long count();
}
