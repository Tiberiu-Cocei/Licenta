package com.android.android.dtos;

public class ResetPasswordDto {

    private String username;

    private String passwordResetCode;

    private String newPassword;

    public ResetPasswordDto(String username, String passwordResetCode, String newPassword) {
        this.username = username;
        this.passwordResetCode = passwordResetCode;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
