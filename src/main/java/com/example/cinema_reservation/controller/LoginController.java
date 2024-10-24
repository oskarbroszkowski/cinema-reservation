package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.Role;
import com.example.cinema_reservation.model.User;
import com.example.cinema_reservation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class LoginController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private RequestCache requestCache;

    @Autowired
    public LoginController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.requestCache = new HttpSessionRequestCache();
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", model.getAttribute("errorMessage"));
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpServletRequest request, RedirectAttributes redirectAttributes) {
        UsernamePasswordAuthenticationToken authenticationRequest =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            Optional<User> optionalUser = userService.findUserByUsername(username);
            User user = optionalUser.get();

            if (!user.isEnabled()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Your account has been blocked.");
                return "redirect:/login";
            }
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            User authenticatedUser = (User) authenticationResponse.getPrincipal();

            if (authenticatedUser.getRoles().contains(Role.ROLE_ADMIN)) {
                return "redirect:/admin/dashboard";
            }

            SavedRequest savedRequest = requestCache.getRequest(request, null);
            if (savedRequest != null) {
                return "redirect:" + savedRequest.getRedirectUrl();
            }

            return "redirect:/homepage";

        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
            return "redirect:/login";
        }
    }

}
