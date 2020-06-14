package com.thesis.webapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "station_id")
    private UUID stationId;

    @Column(name = "day")
    private Date day;

    @Column(name = "hour_from")
    private int hourFrom;

    @Column(name = "hour_to")
    private int hourTo;

    @Column(name = "bicycles_taken")
    private int bicyclesTaken;

    @Column(name = "bicycles_brought")
    private int bicyclesBrought;

    @Column(name = "discounts_from")
    private int discountsFrom;

    @Column(name = "discounts_to")
    private int discountsTo;

    @Column(name = "was_station_empty")
    private boolean wasStationEmpty;

    @Column(name = "was_station_full")
    private boolean wasStationFull;

    @Column(name = "times_clicked_while_empty")
    private int timesClickedWhileEmpty;

    @Column(name = "times_clicked_while_full")
    private int timesClickedWhileFull;

    public Activity() {}

    public Activity(UUID stationId, Date day, int hourFrom) {
        this.id = UUID.randomUUID();
        this.stationId = stationId;
        this.day = day;
        this.hourFrom = hourFrom;
        this.hourTo = hourFrom + 1;
        this.bicyclesTaken = 0;
        this.bicyclesBrought = 0;
        this.discountsFrom = 0;
        this.discountsTo = 0;
        this.wasStationEmpty = false;
        this.wasStationFull = false;
    }

    public UUID getId() {
        return id;
    }

    public UUID getStationId() {
        return stationId;
    }

    public Date getDay() {
        return day;
    }

    public int getHourFrom() {
        return hourFrom;
    }

    public int getHourTo() {
        return hourTo;
    }

    public int getBicyclesTaken() {
        return bicyclesTaken;
    }

    public int getBicyclesBrought() {
        return bicyclesBrought;
    }

    public int getDiscountsFrom() {
        return discountsFrom;
    }

    public int getDiscountsTo() {
        return discountsTo;
    }

    public boolean wasStationEmpty() {
        return wasStationEmpty;
    }

    public boolean wasStationFull() {
        return wasStationFull;
    }

    public int getTimesClickedWhileEmpty() {
        return timesClickedWhileEmpty;
    }

    public int getTimesClickedWhileFull() {
        return timesClickedWhileFull;
    }

    public void incrementTimesClickedWhileEmpty() {
        this.timesClickedWhileEmpty++;
    }

    public void incrementTimesClickedWhileFull() {
        this.timesClickedWhileFull++;
    }

    public void setBicyclesTaken(int bicyclesTaken) {
        this.bicyclesTaken = bicyclesTaken;
    }

    public void setBicyclesBrought(int bicyclesBrought) {
        this.bicyclesBrought = bicyclesBrought;
    }

    public void setDiscountsFrom(int discountsFrom) {
        this.discountsFrom = discountsFrom;
    }

    public void setDiscountsTo(int discountsTo) {
        this.discountsTo = discountsTo;
    }

    public void setTimesClickedWhileEmpty(int timesClickedWhileEmpty) {
        this.timesClickedWhileEmpty = timesClickedWhileEmpty;
    }

    public void setTimesClickedWhileFull(int timesClickedWhileFull) {
        this.timesClickedWhileFull = timesClickedWhileFull;
    }

    public void setWasStationEmpty(boolean wasStationEmpty) {
        this.wasStationEmpty = wasStationEmpty;
    }

    public void setWasStationFull(boolean wasStationFull) {
        this.wasStationFull = wasStationFull;
    }
}
