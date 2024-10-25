package com.example.cinema_reservation.serviceimpl;

import com.example.cinema_reservation.model.CinemaHall;
import com.example.cinema_reservation.model.Screening;
import com.example.cinema_reservation.repository.CinemaHallRepository;
import com.example.cinema_reservation.repository.ScreeningRepository;
import com.example.cinema_reservation.service.CinemaHallService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaHallServiceImpl implements CinemaHallService {

    private final CinemaHallRepository cinemaHallRepository;
    private final ScreeningRepository screeningRepository;

    @Autowired
    public CinemaHallServiceImpl(CinemaHallRepository cinemaHallRepository,
                                 ScreeningRepository screeningRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
        this.screeningRepository = screeningRepository;
    }

    public List<CinemaHall> findAllCinemaHalls() {
        return cinemaHallRepository.findAll();
    }

    public Optional<CinemaHall> findCinemaHallById(Long id) {
        return cinemaHallRepository.findById(id);
    }

    public Optional<CinemaHall> findCinemaHallByName(String name) {
        return cinemaHallRepository.findByName(name);
    }

    public CinemaHall saveCinemaHall(CinemaHall cinemaHall) {
        return cinemaHallRepository.save(cinemaHall);
    }

    public CinemaHall updateCinemaHall(Long id, CinemaHall updatedCinemaHall) {
        return cinemaHallRepository.findById(id)
                .map(cinemaHall -> {
                    cinemaHall.setName(updatedCinemaHall.getName());
                    cinemaHall.setRows(updatedCinemaHall.getRows());
                    cinemaHall.setColumns(updatedCinemaHall.getColumns());
                    cinemaHall.setHallType(updatedCinemaHall.getHallType());
                    return cinemaHallRepository.save(cinemaHall);
                })
                .orElseThrow(() -> new RuntimeException("Cinema hall not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteCinemaHallById(Long id) {
        Optional<CinemaHall> optionalCinemaHall = cinemaHallRepository.findById(id);

        if (optionalCinemaHall.isPresent()) {
            List<Screening> screenings = screeningRepository.findAllByCinemaHallId(id);

            if (!screenings.isEmpty()) {
                screeningRepository.deleteAll(screenings);
            }
            cinemaHallRepository.deleteById(id);
        }
    }

        @Override
        public int cinemaHallCount () {
            return (int) cinemaHallRepository.count();
        }
    }


