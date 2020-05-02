package com.thesis.webapi.entities;

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

    public UUID getStaffId() {
        return staffId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
