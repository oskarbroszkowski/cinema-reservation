package com.example.cinema_reservation.repository;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    @Query("SELECT s FROM Screening s WHERE s.cinemaHall.id = :cinemaHallId AND s.screeningDate = :screeningDate")
    List<Screening> findAllByCinemaHallIdAndDate(@Param("cinemaHallId") Long cinemaHallId, @Param("screeningDate") LocalDate screeningDate);
    List<Screening> findByMovie(Movie movie);
    void deleteByMovieId(Long movieId);
    long count();
}
