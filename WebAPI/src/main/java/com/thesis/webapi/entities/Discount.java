package com.thesis.webapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "discount")
public class Discount {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "from_station_id")
    private UUID fromStationId;

    @Column(name = "to_station_id")
    private UUID toStationId;

    @Column(name = "discounts_left")
    private Integer discountsLeft;

    @Column(name = "discount_value")
    private Double discountValue;

    @Column(name="start_time")
    private Date startTime;

    @Column(name="end_time")
    private Date endTime;

    public UUID getId() {
        return id;
    }

    public UUID getFromStationId() {
        return fromStationId;
    }

    public UUID getToStationId() {
        return toStationId;
    }

    public Integer getDiscountsLeft() {
        return discountsLeft;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}