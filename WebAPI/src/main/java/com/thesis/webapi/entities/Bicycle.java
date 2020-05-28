package com.thesis.webapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "bicycle")
public class Bicycle {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "station_id")
    private UUID stationId;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "status", updatable = false)
    private String status;

    @Column(name = "model")
    private String model;

    @Column(name = "lock_number")
    private Integer lockNumber;

    public Bicycle() {}

    public Bicycle(UUID stationId, String model) {
        this.id = UUID.randomUUID();
        this.stationId = stationId;
        this.arrivalTime = null;
        this.status = null;
        this.model = model;
        this.lockNumber = 1;
    }

    public UUID getId() {
        return id;
    }

    public UUID getStationId() {
        return stationId;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public String getModel() {
        return model;
    }

    public Integer getLockNumber() {
        return lockNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setStationId(UUID stationId) {
        this.stationId = stationId;
    }
}
