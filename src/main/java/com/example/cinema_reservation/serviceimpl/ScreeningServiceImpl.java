package com.example.cinema_reservation.serviceimpl;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.model.Screening;
import com.example.cinema_reservation.repository.ScreeningRepository;
import com.example.cinema_reservation.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;

    @Autowired
    public ScreeningServiceImpl(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    public List<Screening> findAllScreenings() {
        return screeningRepository.findAll();
    }

    public Optional<Screening> findScreeningById(Long id) {
        return screeningRepository.findById(id);
    }

    public Screening saveScreening(Screening screening) {
        return screeningRepository.save(screening);
    }

    public void deleteScreeningById(Long id) {
        screeningRepository.deleteById(id);
    }

    public List<Screening> findScreeningByMovie(Movie movie) {
        return screeningRepository.findByMovie(movie);
    }
}
