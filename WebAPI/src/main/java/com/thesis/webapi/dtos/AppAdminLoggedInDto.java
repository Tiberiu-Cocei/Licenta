package com.thesis.webapi.dtos;

import com.thesis.webapi.entities.AppAdmin;

import java.util.UUID;

public class AppAdminLoggedInDto {

    private UUID staffId;

    private String username;

    private UUID authenticationToken;

    public AppAdminLoggedInDto(AppAdmin appAdmin) {
        this.staffId = appAdmin.getStaffId();
        this.username = appAdmin.getUsername();
        this.authenticationToken = appAdmin.getAuthenticationToken();
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

    public UUID getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(UUID authenticationToken) {
        this.authenticationToken = authenticationToken;
    }
}
