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
    private Double basePrice;

    @Column(name="interval_price")
    private Double intervalPrice;

    @Column(name="interval_time")
    private Integer intervalTime;

    @Column(name="discounts_used")
    private Boolean discountsUsed;

    @Column(name="discount_value")
    private Double discountValue;

    @Column(name="transports_used")
    private Boolean transportsUsed;

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

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getIntervalPrice() {
        return intervalPrice;
    }

    public void setIntervalPrice(Double intervalPrice) {
        this.intervalPrice = intervalPrice;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public Boolean areDiscountsUsed() {
        return discountsUsed;
    }

    public void setDiscountsUsed(Boolean discountsUsed) {
        this.discountsUsed = discountsUsed;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Boolean areTransportsUsed() {
        return transportsUsed;
    }

    public void setTransportsUsed(Boolean transportsUsed) {
        this.transportsUsed = transportsUsed;
    }

    public Boolean getDiscountsUsed() {
        return discountsUsed;
    }

    public Boolean getTransportsUsed() {
        return transportsUsed;
    }
}
