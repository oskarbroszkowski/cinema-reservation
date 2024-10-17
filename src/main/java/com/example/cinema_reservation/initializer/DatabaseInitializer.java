package com.example.cinema_reservation.initializer;

import com.example.cinema_reservation.model.*;
import com.example.cinema_reservation.repository.CinemaHallRepository;
import com.example.cinema_reservation.repository.MovieRepository;
import com.example.cinema_reservation.repository.ScreeningRepository;
import com.example.cinema_reservation.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class DatabaseInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MovieRepository movieRepository;
    private final CinemaHallRepository cinemaHallRepository;
    private final ScreeningRepository screeningRepository;

    @Autowired
    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, MovieRepository movieRepository,
    CinemaHallRepository cinemaHallRepository, ScreeningRepository screeningRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.movieRepository = movieRepository;
        this.cinemaHallRepository = cinemaHallRepository;
        this.screeningRepository = screeningRepository;
    }

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ClassPathResource resource = new ClassPathResource("data.json");

        DataFile dataFile = objectMapper.readValue(resource.getFile(), DataFile.class);

        List<User> users = dataFile.getUsers();
        for (User user : users) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setMatchingPassword(user.getPassword());
        }
        userRepository.saveAll(users);

        List<Movie> movies = dataFile.getMovies();
        movieRepository.saveAll(movies);

        List<CinemaHall> cinemaHalls = dataFile.getCinemaHalls();
        cinemaHallRepository.saveAll(cinemaHalls);

        List<Screening> screenings = dataFile.getScreenings();
        screeningRepository.saveAll(screenings);
    }
}
