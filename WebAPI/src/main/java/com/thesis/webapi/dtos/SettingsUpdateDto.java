package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class SettingsUpdateDto {

    @NotNull(message = "City id cannot be null.")
    private UUID cityId;

    private Double basePrice;

    private Double intervalPrice;

    private Integer intervalTime;

    private Boolean discountsUsed;

    private Double discountValue;

    private Boolean transportsUsed;

    public Double getBasePrice() {
        return basePrice;
    }

    public Double getIntervalPrice() {
        return intervalPrice;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public Boolean getDiscountsUsed() {
        return discountsUsed;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public Boolean getTransportsUsed() {
        return transportsUsed;
    }

    public UUID getCityId() {
        return cityId;
    }
}
