package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.CinemaHall;
import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.model.Screening;
import com.example.cinema_reservation.repository.ScreeningRepository;
import com.example.cinema_reservation.service.CinemaHallService;
import com.example.cinema_reservation.service.MovieService;
import com.example.cinema_reservation.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/screenings")
public class AdminScreeningsController {
    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final CinemaHallService cinemaHallService;
    private final ScreeningRepository screeningRepository;

    @Autowired
    public AdminScreeningsController(MovieService movieService, ScreeningService screeningService,
                                     CinemaHallService cinemaHallService, ScreeningRepository screeningRepository) {
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.cinemaHallService = cinemaHallService;
        this.screeningRepository = screeningRepository;
    }

    @GetMapping
    public String showScreenings(Model model) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Screening> screenings = screeningService.findAllScreenings();

        for (Screening screening : screenings) {
            screening.setFormattedScreeningDate(screening.getScreeningDate().format(formatter));
        }

        model.addAttribute("screenings", screenings);

        List<Movie> movies = movieService.findAllMovies();

        for (Movie movie : movies) {
            movie.setFormattedReleaseDate(movie.getReleaseDate().format(formatter));
        }

        model.addAttribute("movies", movies);

        List<CinemaHall> cinemaHalls = cinemaHallService.findAllCinemaHalls();

        model.addAttribute("cinemaHalls", cinemaHalls);
        return "admin-screenings";
    }

    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        screeningService.deleteScreeningById(id);
        return "redirect:/admin/screenings";
    }

    @PostMapping("/add")
    public String addScreening(@RequestParam("movieId") Long movieId,
                               @RequestParam("screeningDate") LocalDate screeningDate,
                               @RequestParam("screeningTime") LocalTime screeningTime,
                               @RequestParam("cinemaHallId") Long cinemaHallId,
                               Model model) {

        Optional<Movie> optionalMovie = movieService.findMovieById(movieId);
        Optional<CinemaHall> optionalCinemaHall = cinemaHallService.findCinemaHallById(cinemaHallId);

        if (optionalMovie.isPresent() && optionalCinemaHall.isPresent()) {
            Movie movie = optionalMovie.get();
            CinemaHall cinemaHall = optionalCinemaHall.get();

            if (isScreeningConflict(screeningDate, screeningTime, cinemaHallId, movieId)) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                List<Screening> screenings = screeningService.findAllScreenings();

                for (Screening screening : screenings) {
                    screening.setFormattedScreeningDate(screening.getScreeningDate().format(formatter));
                }

                model.addAttribute("screenings", screenings);
                model.addAttribute("error", "Conflict: A screening already exists in the selected cinema hall at this time.");
                model.addAttribute("movies", movieService.findAllMovies());
                model.addAttribute("cinemaHalls", cinemaHallService.findAllCinemaHalls());
                return "admin-screenings";
            }

            Screening screening = new Screening(movie, screeningDate, screeningTime, cinemaHall);
            screeningService.saveScreening(screening);
        }
            return "redirect:/admin/screenings";
    }

    @PostMapping("/edit/{id}")
    public String editScreening(@PathVariable Long id,
                                @RequestParam String screeningDate,
                                @RequestParam String time,
                                @RequestParam Long movieId,
                                @RequestParam Long cinemaHallId,
                                RedirectAttributes redirectAttributes) {

        Optional<Screening> optionalScreening = screeningService.findScreeningById(id);
        if (optionalScreening.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Screening not found.");
            return "redirect:/admin/screenings";
        }

        Screening screening = optionalScreening.get();
        boolean updated = false;

        if (screeningDate != null && !screeningDate.trim().isEmpty() &&
                !screeningDate.equals(screening.getScreeningDate().toString())) {
            screening.setScreeningDate(LocalDate.parse(screeningDate));
            updated = true;
        }

        if (time != null && !time.trim().isEmpty()) {
            LocalTime localTime = LocalTime.parse(time);
            if (!localTime.equals(screening.getScreeningTime())) {
                screening.setScreeningTime(localTime);
                updated = true;
            }
        }

        if (movieId != null && !movieId.equals(screening.getMovie().getId())) {
            Optional<Movie> movie = movieService.findMovieById(movieId);
            movie.ifPresent(screening::setMovie);
            updated = true;
        }

        if (cinemaHallId != null && !cinemaHallId.equals(screening.getCinemaHall().getId())) {
            Optional<CinemaHall> cinemaHall = cinemaHallService.findCinemaHallById(cinemaHallId);
            cinemaHall.ifPresent(screening::setCinemaHall);
            updated = true;
        }

        if (updated) {
            screeningService.saveScreening(screening);
            redirectAttributes.addFlashAttribute("successMessage", "Screening has been updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("infoMessage", "No changes were made.");
        }

        return "redirect:/admin/screenings";
    }

    public boolean isScreeningConflict(LocalDate screeningDate, LocalTime screeningTime, Long cinemaHallId, Long movieId) {

        Optional<Movie> optionalMovie = movieService.findMovieById(movieId);
        if (!optionalMovie.isPresent()) {
            return false;
        }

        Movie movie = optionalMovie.get();
        int movieDuration = movie.getDuration();
        int breakTime = 30;

        LocalTime endScreeningTime = screeningTime.plusMinutes(movieDuration).plusMinutes(breakTime);

        List<Screening> screenings = screeningRepository.findAllByCinemaHallIdAndDate(cinemaHallId, screeningDate);


        for (Screening screening : screenings) {
            LocalTime existingScreeningStartTime = screening.getScreeningTime();

            LocalTime existingScreeningEndTime = existingScreeningStartTime.plusMinutes(screening.getMovie().getDuration()).plusMinutes(30);

            if ((screeningTime.isBefore(existingScreeningEndTime) && endScreeningTime.isAfter(existingScreeningStartTime)) ||
                    (screeningTime.equals(existingScreeningStartTime) || endScreeningTime.equals(existingScreeningEndTime))) {
                return true;
            }
        }

        return false;
    }
}
