package com.thesis.webapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "predicted_activity")
public class PredictedActivity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "station_id")
    private UUID stationId;

    @Column(name = "day")
    private Date day;

    @Column(name = "hour")
    private int hour;

    @Column(name = "number_of_bicycles")
    private int numberOfBicycles;

    public UUID getId() {
        return id;
    }

    public UUID getStationId() {
        return stationId;
    }

    public Date getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getNumberOfBicycles() {
        return numberOfBicycles;
    }
}
