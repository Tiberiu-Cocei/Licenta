package com.thesis.webapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;
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
    private LocalTime arrivalTime;

    @Column(name = "lock_number")
    private int lockNumber;

    public TransportLine() {}

    public TransportLine(UUID transportId, UUID bicycleId, UUID stationFromId, UUID stationToId, LocalTime arrivalTime) {
        this.id = UUID.randomUUID();
        this.transportId = transportId;
        this.bicycleId = bicycleId;
        this.stationFromId = stationFromId;
        this.stationToId = stationToId;
        this.arrivalTime = arrivalTime;
        this.lockNumber = 1;
    }

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

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public int getLockNumber() {
        return lockNumber;
    }
}
