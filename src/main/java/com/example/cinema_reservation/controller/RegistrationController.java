package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.config.SecurityConstants;
import com.example.cinema_reservation.exception.UserAlreadyExistException;
import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(HttpServletRequest webRequest, Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@ModelAttribute("user") @Valid User user,
                                               @RequestParam(required = false) String adminPassword,
                                               @RequestParam(required = false) Boolean isEmployeeRegistration,
                                               HttpServletRequest request, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid registration data");
        }

        if (isEmployeeRegistration != null && isEmployeeRegistration) {
            if (adminPassword == null || !adminPassword.equals(SecurityConstants.ADMIN_PASSWORD)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Invalid admin password.");
            }
        }

        try {
            if(adminPassword != null && adminPassword.equals(SecurityConstants.ADMIN_PASSWORD)) {
                userService.registerAdmin(user);

            } else {
                userService.registerUser(user);
            }

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/register-success")).build();

        } catch (UserAlreadyExistException uaeEx) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("An account for that username/email already exists.");
        }
    }
}