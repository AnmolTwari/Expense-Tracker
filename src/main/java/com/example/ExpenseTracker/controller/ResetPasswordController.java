package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResetPasswordController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam("token") String token, Model model) {
        User user = userRepository.findByResetToken(token);

        if (user == null) {
            model.addAttribute("error", "Invalid token");
            return "reset-password";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleReset(@RequestParam String token,
                              @RequestParam String password,
                              @RequestParam String confirmPassword,
                              Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("token", token);
            return "reset-password";
        }

        User user = userRepository.findByResetToken(token);
        if (user == null) {
            model.addAttribute("error", "Invalid token");
            return "reset-password";
        }

        user.setPassword(password); // Ideally encode with BCrypt
        user.setResetToken(null);
        userRepository.save(user);

        return "redirect:/login?resetSuccess";
    }

}
