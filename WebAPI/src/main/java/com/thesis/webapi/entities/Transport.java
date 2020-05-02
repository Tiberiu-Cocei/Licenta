package com.thesis.webapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transport")
public class Transport {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "staff_id")
    private UUID staffId;

    @Column(name = "date")
    private Date date;

    public UUID getId() {
        return id;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public Date getDate() {
        return date;
    }
}
