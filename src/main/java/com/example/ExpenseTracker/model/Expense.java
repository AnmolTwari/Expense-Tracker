package com.example.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double amount;

    private LocalDate date;

    private String category; // "Income" or "Expense"

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // Link this expense to a specific user
}
