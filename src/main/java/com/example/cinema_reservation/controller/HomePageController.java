package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
public class HomePageController {

    private final MovieService movieService;
    private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);

    @Autowired
    public HomePageController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/homepage")
    public String homepage(Model model, @AuthenticationPrincipal User user) {
        List<Movie> movies = movieService.findAllMovies();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Movie movie : movies) {
            movie.setFormattedReleaseDate(movie.getReleaseDate().format(formatter));
        }

        model.addAttribute("movies", movies);

        if (user != null) {
            model.addAttribute("name", user.getFirstName());
            logger.info("User logged in: " + user.getFirstName());
        } else {
            model.addAttribute("name", "Guest");
        }

        return "homepage";
    }
}