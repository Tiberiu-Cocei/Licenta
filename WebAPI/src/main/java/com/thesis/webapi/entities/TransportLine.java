package com.thesis.webapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transport_line")
public class TransportLine {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "transport_id")
    private UUID transportId;

    @Column(name = "bicycle_id")
    private UUID bicycleId;

    @Column(name = "station_from")
    private UUID stationFromId;

    @Column(name = "station_to")
    private UUID stationToId;

    @Column(name = "arrival_time")
    private Date arrivalTime;

    @Column(name = "lock_number")
    private int lockNumber;

    public UUID getId() {
        return id;
    }

    public UUID getTransportId() {
        return transportId;
    }

    public UUID getBicycleId() {
        return bicycleId;
    }

    public UUID getStationFromId() {
        return stationFromId;
    }

    public UUID getStationToId() {
        return stationToId;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public int getLockNumber() {
        return lockNumber;
    }
}
