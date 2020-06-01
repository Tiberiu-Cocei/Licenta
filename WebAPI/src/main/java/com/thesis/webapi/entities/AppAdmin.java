package com.thesis.webapi.entities;

import com.thesis.webapi.dtos.AppAdminCreateDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "app_admin")
public class AppAdmin {

    @Id
    @Column(name = "staff_id")
    private UUID staffId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name="authentication_token")
    private UUID authenticationToken;

    @Column(name="salt")
    private String salt;

    public AppAdmin() {}

    public AppAdmin(AppAdminCreateDto appAdminCreateDto) {
        this.staffId = appAdminCreateDto.getStaffId();
        this.username = appAdminCreateDto.getUsername();
        this.password = appAdminCreateDto.getPassword();
        this.authenticationToken = null;
        this.salt = null;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UUID getAuthenticationToken() {
        return authenticationToken;
    }

    public String getSalt() {
        return salt;
    }

    public void setAuthenticationToken(UUID authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
