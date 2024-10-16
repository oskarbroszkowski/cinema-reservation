package com.example.cinema_reservation.service;

import com.example.cinema_reservation.model.Movie;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MovieService {

    List<Movie> findAllMovies();
    Optional<Movie> findMovieById(Long id);
    Movie saveMovie(Movie movie);
    void deleteMovie(Long id);
}
