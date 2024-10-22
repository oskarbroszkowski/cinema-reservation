package com.example.cinema_reservation.serviceimpl;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.repository.MovieRepository;
import com.example.cinema_reservation.repository.ScreeningRepository;
import com.example.cinema_reservation.service.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningsRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, ScreeningRepository screeningsRepository) {
        this.movieRepository = movieRepository;
        this.screeningsRepository = screeningsRepository;
    }

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
    @Transactional
    public boolean deleteMovie(Long id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);

        if (movieOptional.isPresent()) {
            screeningsRepository.deleteByMovieId(id);
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public int movieCount() {
        return (int) movieRepository.count();
    }
}
