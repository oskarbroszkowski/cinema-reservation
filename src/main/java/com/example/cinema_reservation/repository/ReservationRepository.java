package com.example.cinema_reservation.repository;

import com.example.cinema_reservation.model.Reservation;
import com.example.cinema_reservation.model.Screening;
import com.example.cinema_reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByScreeningAndSeatRowAndSeatColumn(Screening screening, int seatRow, int seatColumn);
    List<Reservation> findByUser(User user);
    List<Reservation> findByScreening(Screening screening);
}
