package com.example.cinema_reservation.service;

import com.example.cinema_reservation.model.Reservation;
import com.example.cinema_reservation.model.Screening;
import com.example.cinema_reservation.model.User;

import java.util.List;

public interface ReservationService {
    Reservation saveReservation(Reservation reservation);
    List<Reservation> findReservationsByUser(User user);
    List<Reservation> findReservationsByScreening(Screening screening);
    boolean deleteReservationById(Long id);
}
