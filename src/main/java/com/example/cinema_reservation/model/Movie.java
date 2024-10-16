package com.example.cinema_reservation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int duration;

    @Column(length = 1000)
    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private String thumbnailPath;

    @Transient
    private String formattedReleaseDate;

    public Movie() {}

    public Movie(String title, String genre, int duration, String description, LocalDate releaseDate, double rating) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }
}
