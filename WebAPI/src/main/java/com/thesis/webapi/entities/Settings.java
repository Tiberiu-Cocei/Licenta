package com.thesis.webapi.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @Column(name="city_id")
    private UUID cityId;

    @Column(name="base_price")
    private double basePrice;

    @Column(name="interval_price")
    private double intervalPrice;

    @Column(name="interval_time")
    private int intervalTime;

    @Column(name="discounts_used")
    private boolean discountsUsed;

    @Column(name="discount_value")
    private double discountValue;

    @Column(name="transports_used")
    private boolean transportsUsed;

    public Settings() {}

    public Settings(UUID cityId, double basePrice, double intervalPrice, int intervalTime) {
        this.cityId = cityId;
        this.basePrice = basePrice;
        this.intervalPrice = intervalPrice;
        this.intervalTime = intervalTime;
        this.discountsUsed = false;
        this.discountValue = 0.0;
        this.transportsUsed = false;
    }

    public UUID getCityId() {
        return cityId;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getIntervalPrice() {
        return intervalPrice;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public boolean areDiscountsUsed() {
        return discountsUsed;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public boolean areTransportsUsed() {
        return transportsUsed;
    }
}
