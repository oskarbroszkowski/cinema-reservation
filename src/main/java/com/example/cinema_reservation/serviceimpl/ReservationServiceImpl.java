package com.example.cinema_reservation.serviceimpl;

import com.example.cinema_reservation.model.Reservation;
import com.example.cinema_reservation.model.Screening;
import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.repository.ReservationRepository;
import com.example.cinema_reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> findReservationsByUser(User user) {
        return reservationRepository.findReservationsByUser(user);
    }

    @Override
    public List<Reservation> findReservationsByScreening(Screening screening) {
        return reservationRepository.findReservationsByScreening(screening);
    }

    @Override
    public boolean deleteReservationById(Long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isPresent()) {
            reservationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
