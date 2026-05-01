package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repo;

    public List<Expense> listAll() {
        return repo.findAll();
    }

    public void save(Expense expense) {
        repo.save(expense);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Expense get(Long id) {
        return repo.findById(id).orElse(null);
    }

    public double getTotalIncome() {
        return repo.findAll().stream()
                .filter(e -> "Income".equalsIgnoreCase(e.getCategory()))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double getTotalExpense() {
        return repo.findAll().stream()
                .filter(e -> "Expense".equalsIgnoreCase(e.getCategory()))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double getTotalBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    public List<Expense> getAllExpenses() {
        return repo.findAll();
    }

    public List<Expense> getExpensesByUser(User user) {
        return repo.findByUser(user);
    }

    public List<Expense> searchExpensesByUser(User user, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getExpensesByUser(user);
        }

        return repo.searchByUserAndKeyword(user, keyword.trim());
    }

    public List<Expense> filterExpensesByUser(User user, String keyword, String category) {
        if ((keyword == null || keyword.trim().isEmpty()) && (category == null || category.trim().isEmpty())) {
            return repo.findByUser(user);
        }

        return repo.filterByUserAndCriteria(user,
                keyword == null ? null : keyword.trim(),
                category == null ? null : category.trim());
    }


}

