package com.thesis.webapi.dtos;

import com.thesis.webapi.entities.AppUser;

import java.util.UUID;

public class AppUserLoggedInDto {

    private UUID id;

    private UUID bicycleId;

    private String email;

    private String username;

    private int warningCount;

    private boolean banned;

    private UUID authenticationToken;

    private UUID paymentMethodId;

    public AppUserLoggedInDto() {}

    public AppUserLoggedInDto(AppUser appUser) {
        this.id = appUser.getId();
        this.bicycleId = appUser.getBicycleId();
        this.email = appUser.getEmail();
        this.username = appUser.getUsername();
        this.warningCount = appUser.getWarningCount();
        this.banned = appUser.isBanned();
        this.authenticationToken = appUser.getAuthenticationToken();
        this.paymentMethodId = appUser.getPaymentMethod().getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBicycleId() {
        return bicycleId;
    }

    public void setBicycleId(UUID bicycleId) {
        this.bicycleId = bicycleId;
    }

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

    public int getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public UUID getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(UUID authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}
