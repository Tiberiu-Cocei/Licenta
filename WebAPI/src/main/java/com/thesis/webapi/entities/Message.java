package com.thesis.webapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "admin_id")
    private UUID adminId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private Date date;

    @Column(name = "seen")
    private boolean seen;

    public Message() {}

    public Message(UUID id, UUID userId, String description) {
        this.id = id;
        this.adminId = UUID.randomUUID();
        this.userId = userId;
        this.description = description;
        this.date = null;
        this.seen = false;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}