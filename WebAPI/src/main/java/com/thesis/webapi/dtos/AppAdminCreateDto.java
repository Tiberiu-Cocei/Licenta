package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public class AppAdminCreateDto {

    @NotNull(message = "Staff id cannot be null.")
    private UUID staffId;

    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters.")
    private String username;

    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters.")
    private String password;

    public AppAdminCreateDto(UUID staffId, String username, String password) {
        this.staffId = staffId;
        this.username = username;
        this.password = password;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
