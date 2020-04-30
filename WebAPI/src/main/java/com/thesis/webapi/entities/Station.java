package com.thesis.webapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "city_id")
    private UUID cityId;

    @Column(name = "name")
    private String name;

    @Column(name = "coordinates")
    private String coordinates;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "current_capacity")
    private Integer currentCapacity;

    public UUID getId() {
        return id;
    }

    public UUID getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }
}
