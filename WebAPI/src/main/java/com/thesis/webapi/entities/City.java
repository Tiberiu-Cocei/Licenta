package com.thesis.webapi.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "city")
public class City {

    @Id
    @Column(name="id")
    private UUID cityId;

    @Column(name="name")
    private String name;

    public City() {}

    public City(UUID cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    public UUID getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

}
