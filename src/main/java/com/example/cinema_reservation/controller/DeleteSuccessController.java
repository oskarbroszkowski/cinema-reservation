package com.example.cinema_reservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeleteSuccessController {

    @GetMapping("/delete-success")
    public String delete(HttpServletRequest request, Model model) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        model.addAttribute("message", "Your account has been successfully deleted");

        return "delete-success";
    }
}