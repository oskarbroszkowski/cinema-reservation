package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.Screening;
import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.service.ScreeningService;
import com.example.cinema_reservation.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ScreeningsController {

    private final ScreeningService screeningService;
    private final MovieService movieService;

    @Autowired
    public ScreeningsController(ScreeningService screeningService, MovieService movieService) {
        this.screeningService = screeningService;
        this.movieService = movieService;
    }

    @GetMapping("/screenings")
    public String getCurrentScreenings(Model model, @AuthenticationPrincipal User user) {
        List<Movie> movies = movieService.findAllMovies();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Movie movie : movies) {
            movie.setFormattedReleaseDate(movie.getReleaseDate().format(formatter));
        }

        model.addAttribute("movies", movies);

        List<Screening> screenings = screeningService.findAllScreenings();
        DateTimeFormatter screeningFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Screening screening : screenings) {
            screening.setFormattedScreeningDate(screening.getScreeningDate().format(screeningFormatter));
        }

        model.addAttribute("screenings", screenings);

        if (user != null) {
            model.addAttribute("name", user.getFirstName());
        } else {
            model.addAttribute("name", "Guest");
        }

        return "screenings";
    }
}
