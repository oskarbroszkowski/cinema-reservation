package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.model.Screening;
import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.service.CinemaHallService;
import com.example.cinema_reservation.service.MovieService;
import com.example.cinema_reservation.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ScreeningsController {

    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final CinemaHallService cinemaHallService;

    @Autowired
    public ScreeningsController(ScreeningService screeningService, MovieService movieService,
                                CinemaHallService cinemaHallService) {
        this.screeningService = screeningService;
        this.movieService = movieService;
        this.cinemaHallService = cinemaHallService;
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

    @GetMapping("/screenings/data/{id}")
    @ResponseBody
    public Map<String, Object> getScreeningById(@PathVariable Long id) {
        Optional<Screening> optionalScreening = screeningService.findScreeningById(id);

        if (optionalScreening.isPresent()) {

            Screening screening = optionalScreening.get();

            Map<String, Object> response = new HashMap<>();
            response.put("movieId", screening.getMovie().getId());
            response.put("screeningDate", screening.getScreeningDate());
            response.put("time", screening.getScreeningTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            response.put("cinemaHallId", screening.getCinemaHall().getId());

            return response;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found");
        }
    }
}
