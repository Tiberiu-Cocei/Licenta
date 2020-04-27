package com.thesis.webapi.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thesis.webapi.dtos.AppUserCreateDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @Column(name="id")
    private UUID id;

    @Column(name="bicycle_id")
    private UUID bicycleId;

    @Column(name="email")
    private String email;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="warning_count")
    private int warningCount;

    @Column(name="banned")
    private boolean banned;

    @Column(name="authentication_token")
    private UUID authenticationToken;

    @Column(name="password_reset_code")
    private String passwordResetCode;

    @Column(name="salt")
    private String salt;

    @OneToOne
    @JsonManagedReference
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    public AppUser() {}

    public AppUser(AppUserCreateDto appUserCreateDto) {
        this.id = UUID.randomUUID();
        this.bicycleId = null;
        this.email = appUserCreateDto.getEmail();
        this.username = appUserCreateDto.getUsername();
        this.password = appUserCreateDto.getPassword();
        this.warningCount = 0;
        this.banned = false;
        this.authenticationToken = null;
        this.passwordResetCode = null;
        this.salt = null;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String passsword) {
        this.password = passsword;
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

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
