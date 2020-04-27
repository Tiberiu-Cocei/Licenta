package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;

public class AppUserUpdateDto {

    private String email;

    @NotNull(message = "Username cannot be null.")
    private String username;

    @NotNull(message = "Password cannot be null.")
    private String oldPassword;

    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
