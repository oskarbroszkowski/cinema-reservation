package com.example.cinema_reservation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataFile {

    private List<User> users;
    private List<Movie> movies;
    private List<CinemaHall> cinemaHalls;
    private List<Screening> screenings;
}
