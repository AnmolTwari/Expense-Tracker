package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.dto.UserDto;
import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.repository.UserRepository;
import com.example.ExpenseTracker.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/")
    public String viewHomePage(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "category", required = false) String category,
                               Model model,
                               HttpSession session) {
        // Get the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Get all expenses for this user only, or filter by search text/category
        List<Expense> list = service.filterExpensesByUser(loggedInUser, search, category);
        model.addAttribute("listExpenses", list);
        model.addAttribute("searchQuery", search == null ? "" : search);
        model.addAttribute("selectedCategory", category == null ? "" : category);

        // Calculate totals only for this user
        double totalIncome = list.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase("Income"))
                .mapToDouble(Expense::getAmount).sum();

        double totalExpense = list.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase("Expense"))
                .mapToDouble(Expense::getAmount).sum();

        double totalBalance = totalIncome - totalExpense;

        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("user", loggedInUser);

        return "index";
    }




    @GetMapping("/new")
    public String showNewExpenseForm(Model model) {
        model.addAttribute("expense", new Expense());
        return "add_expense";
    }

    @PostMapping("/save")
    public String saveExpense(@ModelAttribute("expense") Expense expense, HttpSession session) {
        User user = (User) session.getAttribute("user");
        expense.setUser(user);
        service.save(expense);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditExpenseForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("expense", service.get(id));
        return "add_expense";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") Long id) {
        service.delete(id);
        return "redirect:/";
    }

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    // Handle registration submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDto") UserDto userDto, Model model) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // no encoder used
        user.setRole("USER");

        userRepository.save(user);

        return "redirect:/login?registered";
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    // Handle login logic
    // Handle login logic
    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            Model model,
                            HttpSession session) {

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        session.setAttribute("user", user);
        return "redirect:/";  // <-- Redirect to index page after login
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear the session
        return "redirect:/login"; // Redirect to login page
    }




}

