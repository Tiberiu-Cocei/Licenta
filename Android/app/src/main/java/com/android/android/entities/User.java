package com.android.android.entities;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class User {
    private static User user = null;

    private UUID id;

    private UUID bicycleId;

    private String email;

    private String username;

    private int warningCount;

    private boolean banned;

    private UUID authenticationToken;

    private PaymentMethod paymentMethod;

    public static void createUser(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        user = objectMapper.readValue(json, User.class);
    }

    public static User getUser() {
        return user;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
