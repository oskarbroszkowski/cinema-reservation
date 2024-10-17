package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.Reservation;
import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getUserReservations(Model model, @AuthenticationPrincipal User user) {

        List<Reservation> reservations = reservationService.findReservationsByUser(user);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Reservation reservation : reservations) {
            reservation.setFormattedScreeningDate(reservation.getScreening().getScreeningDate().format(formatter));
        }

        model.addAttribute("reservations", reservations);

        return "reservations";
    }

    @PostMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);
        return "redirect:/reservations";
    }
}

