package com.android.android.dtos;

public class ResetCodeDto {

    private String username;

    public ResetCodeDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
