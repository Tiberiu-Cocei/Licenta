package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;

public class AppUserResetCodeDto {

    @NotNull(message = "Username cannot be null.")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
