package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AppUserResetPasswordDto {

    @NotNull(message = "Username cannot be null.")
    private String username;

    @NotNull(message = "Password reset code cannot be null.")
    private String passwordResetCode;

    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters.")
    private String newPassword;

    public AppUserResetPasswordDto() {}

    public AppUserResetPasswordDto(String username, String passwordResetCode, String newPassword) {
        this.username = username;
        this.passwordResetCode = passwordResetCode;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
