package com.thesis.webapi.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class AppUserCreateDto {

    @Email(message = "Invalid email address.")
    private String email;

    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters.")
    private String username;

    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters.")
    private String password;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
