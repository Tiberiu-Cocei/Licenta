package com.thesis.webapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "inspection")
public class Inspection {

    @Id
    @Column(name="report_id")
    private UUID reportId;

    @Column(name="staff_id")
    private UUID staffId;

    @Column(name="description")
    private String description;

    @Column(name="fake")
    private boolean fake;

    @Column(name="date")
    private Date date;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "report_id")
    private Report report;

    public UUID getReportId() {
        return reportId;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFake() {
        return fake;
    }

    public Date getDate() {
        return date;
    }
}
