package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.*;
import com.example.cinema_reservation.service.CinemaHallService;
import com.example.cinema_reservation.service.ReservationService;
import com.example.cinema_reservation.service.ScreeningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    private final CinemaHallService cinemaHallService;
    private final ScreeningService screeningService;
    private final ReservationService reservationService;

    @Autowired
    public BookingController(CinemaHallService cinemaHallService,
                             ScreeningService screeningService,
                             ReservationService reservationService) {
        this.cinemaHallService = cinemaHallService;
        this.screeningService = screeningService;
        this.reservationService = reservationService;
    }

    @GetMapping("/book/{screeningId}/{cinemaHallId}")
    public String bookScreening(@PathVariable("screeningId") Long screeningId,
                                @PathVariable("cinemaHallId") Long cinemaHallId,
                                Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<CinemaHall> optionalCinemaHall = cinemaHallService.findCinemaHallById(cinemaHallId);
        Optional<Screening> optionalScreening = screeningService.findScreeningById(screeningId);

        if (optionalCinemaHall.isPresent() && optionalScreening.isPresent()) {
            CinemaHall cinemaHall = optionalCinemaHall.get();
            Screening screening = optionalScreening.get();

            List<Integer> rowIndices = IntStream.range(0, cinemaHall.getRows()).boxed().collect(Collectors.toList());
            List<Integer> columnIndices = IntStream.range(0, cinemaHall.getColumns()).boxed().collect(Collectors.toList());

            Map<String, Boolean> seatReservedMap = new HashMap<>();
            for (int row : rowIndices) {
                for (int col : columnIndices) {
                    seatReservedMap.put(row + "_" + col, seatIsReserved(row, col, screening));
                }
            }

            model.addAttribute("rowIndices", rowIndices);
            model.addAttribute("columnIndices", columnIndices);
            model.addAttribute("cinemaHall", cinemaHall);
            model.addAttribute("screening", screening);
            model.addAttribute("seatReservedMap", seatReservedMap);

            return "book-screening";

        } else {
            model.addAttribute("error", "Cinema hall or screening not found.");
            return "error";
        }
    }

    private boolean seatIsReserved(int row, int column, Screening screening) {
        return reservationService.findByScreening(screening).stream()
                .anyMatch(reservation -> reservation.getSeatRow() == row && reservation.getSeatColumn() == column);
    }

    @PostMapping("/reserve")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<String> reserveSeats(@RequestBody List<Seat> seats) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (seats.isEmpty()) {
            return ResponseEntity.badRequest().body("No seats selected");
        }

        for (Seat seat : seats) {
            if (seat.getScreening() == null || seat.getScreening().getId() == null) {
                logger.warn("Seat {} has no associated screening.", seat);
                return ResponseEntity.badRequest().body("Invalid seat data");
            }

            Optional<Screening> screeningOptional = screeningService.findScreeningById(seat.getScreening().getId());
            if (screeningOptional.isPresent()) {
                Screening screening = screeningOptional.get();

                Reservation reservation = new Reservation();
                reservation.setScreening(screening);
                reservation.setUser(currentUser);
                reservation.setSeatRow(seat.getRow());
                reservation.setSeatColumn(seat.getColumn());

                reservationService.saveReservation(reservation);
            } else {
                logger.warn("Screening not found for seat: {}", seat);
                return ResponseEntity.badRequest().body("Screening not found");
            }
        }
        return ResponseEntity.ok("Seats reserved successfully");
    }
}
