package com.thesis.webapi.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thesis.webapi.dtos.ReportCreateDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @Column(name="id")
    private UUID id;

    @Column(name="user_id")
    private UUID userId;

    @Column(name="bicycle_id")
    private UUID bicycleId;

    @Column(name="severity")
    private int severity;

    @Column(name="description")
    private String description;

    @Column(name="reviewed")
    private Boolean reviewed;

    @Column(name="fake")
    private Boolean fake;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "report")
    @JsonManagedReference
    private Inspection inspection;

    public Report() {}

    public Report(UUID userId, String description) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.bicycleId = UUID.randomUUID();
        this.severity = 0;
        this.description = description;
        this.reviewed = null;
        this.fake = null;
        this.inspection = null;
    }

    public Report(ReportCreateDto reportCreateDto) {
        this.id = UUID.randomUUID();
        this.userId = reportCreateDto.getUserId();
        this.bicycleId = reportCreateDto.getBicycleId();
        this.severity = reportCreateDto.getSeverity();
        this.description = reportCreateDto.getDescription();
        this.reviewed = false;
        this.fake = null;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getBicycleId() {
        return bicycleId;
    }

    public int getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isReviewed() {
        return reviewed;
    }

    public Boolean isFake() {
        return fake;
    }
}
