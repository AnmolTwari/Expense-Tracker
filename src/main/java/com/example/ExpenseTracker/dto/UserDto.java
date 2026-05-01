package com.example.ExpenseTracker.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

}

