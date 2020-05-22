package com.android.android.dtos;

public class UserModifyDto {

    private String email;

    private String username;

    private String oldPassword;

    private String newPassword;

    public UserModifyDto(String email, String username, String oldPassword, String newPassword) {
        this.email = email;
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
