package com.example.cinema_reservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {

        return "admin-dashboard";
    }
}
