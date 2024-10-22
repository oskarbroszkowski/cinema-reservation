package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final UserService userService;
    private final CinemaHallService cinemaHallService;
    private final ReservationService reservationService;

    public AdminDashboardController(MovieService movieService,
                                    ScreeningService screeningService,
                                    UserService userService,
                                    CinemaHallService cinemaHallService,
                                    ReservationService reservationService) {
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.userService = userService;
        this.cinemaHallService = cinemaHallService;
        this.reservationService = reservationService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {

        int moviesCount = movieService.movieCount();
        int screeningsCount = screeningService.screeningsCount();
        int usersCount = userService.usersCount();
        int cinemaHallsCount = cinemaHallService.cinemaHallCount();
        int reservationsCount = reservationService.reservationCount();

        model.addAttribute("moviesCount", moviesCount);
        model.addAttribute("screeningsCount", screeningsCount);
        model.addAttribute("usersCount", usersCount);
        model.addAttribute("cinemaHallsCount", cinemaHallsCount);
        model.addAttribute("reservationsCount", reservationsCount);

        return "admin-dashboard";
    }
}
