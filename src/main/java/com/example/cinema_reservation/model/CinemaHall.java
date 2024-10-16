package com.example.cinema_reservation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cinema_halls")
public class CinemaHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String hallType;

    @Column(nullable = false)
    private int rows;

    @Column(nullable = false)
    private int columns;
}
