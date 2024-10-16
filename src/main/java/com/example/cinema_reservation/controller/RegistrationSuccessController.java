package com.example.cinema_reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationSuccessController {

    @GetMapping("/register-success")
    public String showRegistrationSuccess() {
        return "register-success";
    }
}
