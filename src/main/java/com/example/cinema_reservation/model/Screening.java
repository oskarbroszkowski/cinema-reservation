package com.example.cinema_reservation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "screenings")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "screening_date", nullable = false)
    private LocalDate screeningDate;

    @Column(name = "screening_time", nullable = false)
    private LocalTime screeningTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cinema_hall_id", nullable = false)
    private CinemaHall cinemaHall;

    @Transient
    private String formattedScreeningDate;

    public Screening() {}

    public Screening(Movie movie, LocalDate screeningDate, LocalTime screeningTime,
                     CinemaHall cinemaHall) {
        this.movie = movie;
        this.screeningDate = screeningDate;
        this.screeningTime = screeningTime;
        this.cinemaHall = cinemaHall;
    }
}
