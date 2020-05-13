package com.android.android.dtos;

public class LoginDto {

    private String username;

    private String password;

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //todo: to json method or use GSON?

}
