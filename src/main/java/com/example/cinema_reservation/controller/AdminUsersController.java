package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {
    private final UserService userService;

    @Autowired
    public AdminUsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUsers(Model model) {

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        if (userService.findUserById(id).isPresent()) {
            userService.deleteUserById(id);

        redirectAttributes.addFlashAttribute("successMessage", "User has been deleted successfully!");
    } else {
        redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
    }

        return "redirect:/admin/users";
    }

    @PostMapping("/block/{id}")
    public String blockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<User> optionalUser = userService.findUserById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(false);
            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("successMessage", "User has been blocked successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/unblock/{id}")
    public String unblockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<User> optionalUser = userService.findUserById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("successMessage", "User has been unblocked successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
        }

        return "redirect:/admin/users";
    }
}