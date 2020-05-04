package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

public class AppTransactionPreviewDto {

    private UUID discountId;

    @NotNull(message = "Planned time cannot be null.")
    private Date plannedTime;

    @NotNull(message = "City id cannot be null.")
    private UUID cityId;

    public AppTransactionPreviewDto() {}

    public AppTransactionPreviewDto(Date plannedTime, UUID cityId) {
        this.plannedTime = plannedTime;
        this.cityId = cityId;
        this.discountId = null;
    }

    public UUID getDiscountId() {
        return discountId;
    }

    public Date getPlannedTime() {
        return plannedTime;
    }

    public UUID getCityId() {
        return cityId;
    }
}
