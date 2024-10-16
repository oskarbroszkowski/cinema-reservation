package com.example.cinema_reservation.repository;

import com.example.cinema_reservation.model.Reservation;
import com.example.cinema_reservation.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByScreeningAndSeatRowAndSeatColumn(Screening screening, int seatRow, int seatColumn);
}
