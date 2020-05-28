package com.android.android.entities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Discount {

    private UUID id;

    private UUID fromStationId;

    private UUID toStationId;

    private Integer discountsLeft;

    private Double discountValue;

    private Date startTime;

    private Date endTime;

    public static List<Discount> createDiscountListFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<Discount>>(){});
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFromStationId() {
        return fromStationId;
    }

    public void setFromStationId(UUID fromStationId) {
        this.fromStationId = fromStationId;
    }

    public UUID getToStationId() {
        return toStationId;
    }

    public void setToStationId(UUID toStationId) {
        this.toStationId = toStationId;
    }

    public Integer getDiscountsLeft() {
        return discountsLeft;
    }

    public void setDiscountsLeft(Integer discountsLeft) {
        this.discountsLeft = discountsLeft;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
