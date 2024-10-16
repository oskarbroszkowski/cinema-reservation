package com.example.cinema_reservation.serviceimpl;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.repository.MovieRepository;
import com.example.cinema_reservation.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Optional<Movie> findMovieById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
