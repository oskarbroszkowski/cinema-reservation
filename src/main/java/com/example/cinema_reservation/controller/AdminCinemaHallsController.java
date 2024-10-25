package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.CinemaHall;
import com.example.cinema_reservation.service.CinemaHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/cinemahalls")
public class AdminCinemaHallsController {
    private final CinemaHallService cinemaHallService;

    @Autowired
    public AdminCinemaHallsController(CinemaHallService cinemaHallService) {
        this.cinemaHallService = cinemaHallService;
    }

    @GetMapping
    public String showCinemaHalls(Model model) {

        List<CinemaHall> cinemaHalls = cinemaHallService.findAllCinemaHalls();
        model.addAttribute("cinemaHalls", cinemaHalls);

        return "admin-cinemahalls";
    }

    @GetMapping("/delete/{id}")
    public String deleteCinemaHall(@PathVariable Long id) {
        cinemaHallService.deleteCinemaHallById(id);
        return "redirect:/admin/cinemahalls";
    }

    @PostMapping("/add")
    public String addCinemaHall(@RequestParam("name") int hallNumber,
                                @RequestParam("hallType") String hallType,
                                @RequestParam("rows") int rows,
                                @RequestParam("columns") int columns,
                                Model model) {

        String name = "Hall nr " + hallNumber;

        Optional<CinemaHall> optionalCinemaHall = cinemaHallService.findCinemaHallByName(name);

        if (optionalCinemaHall.isPresent()) {
            model.addAttribute("errorMessage", "A cinema hall with this name already exists.");
            List<CinemaHall> cinemaHalls = cinemaHallService.findAllCinemaHalls();
            model.addAttribute("cinemaHalls", cinemaHalls);
            return "admin-cinemahalls";
        }

        CinemaHall cinemaHall = new CinemaHall(name, hallType, rows, columns);
        cinemaHallService.saveCinemaHall(cinemaHall);

        return "redirect:/admin/cinemahalls";
    }

    @PostMapping("/edit/{id}")
    public String editCinemaHall(@PathVariable Long id,
                                 @RequestParam("name") int hallNumber,
                                 @RequestParam("hallType") String hallType,
                                 @RequestParam("rows") int rows,
                                 @RequestParam("columns") int columns,
                                 RedirectAttributes redirectAttributes) {

        Optional<CinemaHall> optionalCinemaHall = cinemaHallService.findCinemaHallById(id);

        if (optionalCinemaHall.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cinema Hall not found.");
            return "redirect:/admin/cinemahalls";
        }

        CinemaHall cinemaHall = optionalCinemaHall.get();
        String newName = "Hall nr " + hallNumber;

        Optional<CinemaHall> existingHall = cinemaHallService.findCinemaHallByName(newName);
        if (existingHall.isPresent() && !existingHall.get().getId().equals(cinemaHall.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "A cinema hall with this name already exists.");
            return "redirect:/admin/cinemahalls";
        }

        cinemaHall.setName(newName);
        cinemaHall.setHallType(hallType);
        cinemaHall.setRows(rows);
        cinemaHall.setColumns(columns);
        cinemaHallService.saveCinemaHall(cinemaHall);

        redirectAttributes.addFlashAttribute("successMessage", "Cinema Hall has been updated successfully!");
        return "redirect:/admin/cinemahalls";
    }
}

