package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.model.Screening;
import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.service.MovieService;
import com.example.cinema_reservation.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;
    private final ScreeningService screeningService;

    @Autowired
    public MovieController(ScreeningService screeningService, MovieService movieService) {
        this.movieService = movieService;
        this.screeningService = screeningService;
    }

    @GetMapping("/{id}")
    public String getMovieDetails(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal User user) {
        Optional<Movie> optionalMovie = movieService.findMovieById(id);

        if (optionalMovie.isPresent()) {

            Movie movie = optionalMovie.get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            movie.setFormattedReleaseDate(movie.getReleaseDate().format(formatter));
            model.addAttribute("movie", movie);

            List<Screening> screenings = screeningService.findScreeningByMovie(movie);

            for (Screening screening : screenings) {
                screening.setFormattedScreeningDate(screening.getScreeningDate().format(formatter));
            }

            model.addAttribute("screenings", screenings);

            if (user != null) {
                model.addAttribute("name", user.getFirstName());
            } else {
                model.addAttribute("name", "Guest");
            }

            return "movie-details";

        } else {
            model.addAttribute("errorMessage", "Movie not found!");
            return "redirect:/homepage";
        }
    }
}
