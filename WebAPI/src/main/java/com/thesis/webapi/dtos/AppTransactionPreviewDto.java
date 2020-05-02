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
