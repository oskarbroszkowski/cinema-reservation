package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/admin/profile")
    public String showAdminProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "admin-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String username,
                                @RequestParam(required = false) String password,
                                @RequestParam(required = false) String confirmPassword,
                                RedirectAttributes redirectAttributes) {

        boolean updated = false;

        if (!firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
            updated = true;
        }

        if (!lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
            updated = true;
        }

        if (!username.equals(user.getUsername())) {
            user.setUsername(username);
            updated = true;
        }

        if (password != null && !password.isEmpty()) {
            if (confirmPassword == null || confirmPassword.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Confirm password field cannot be empty.");
                return "redirect:/profile";
            }
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match.");
                return "redirect:/profile";
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(password));
                updated = true;
            }
        }

        if (updated) {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Profile has been updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("infoMessage", "No changes were made.");
        }

        return "redirect:/profile";
    }

    @PostMapping("/admin/profile/update")
    public String updateAdminProfile(@AuthenticationPrincipal User user,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String username,
                                @RequestParam(required = false) String password,
                                @RequestParam(required = false) String confirmPassword,
                                RedirectAttributes redirectAttributes) {

        boolean updated = false;

        if (!firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
            updated = true;
        }

        if (!lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
            updated = true;
        }

        if (!username.equals(user.getUsername())) {
            user.setUsername(username);
            updated = true;
        }

        if (password != null && !password.isEmpty()) {
            if (confirmPassword == null || confirmPassword.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Confirm password field cannot be empty.");
                return "redirect:/admin/profile";
            }
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match.");
                return "redirect:/admin/profile";
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(password));
                updated = true;
            }
        }

        if (updated) {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Profile has been updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("infoMessage", "No changes were made.");
        }

        return "redirect:/admin/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(@AuthenticationPrincipal User user) {
        userService.deleteUserById(user.getId());
        return "redirect:/delete-success";
    }

    @PostMapping("/admin/profile/delete")
    public String deleteAdminAccount(@AuthenticationPrincipal User user) {
        userService.deleteUserById(user.getId());
        return "redirect:/delete-success";
    }
}