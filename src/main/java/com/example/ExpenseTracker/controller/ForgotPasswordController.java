package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.repository.UserRepository;
import com.example.ExpenseTracker.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model, HttpServletRequest request) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "Email not found");
            return "forgot-password";
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        String resetUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + "/reset-password?token=" + token;

        emailService.sendResetEmail(user.getEmail(), resetUrl);

        model.addAttribute("message", "Reset link sent to your email.");
        return "forgot-password";
    }
}

