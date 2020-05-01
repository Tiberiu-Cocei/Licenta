package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

public class AppTransactionPreviewDto {

    private UUID discountId;

    @NotNull(message = "Planned time cannot be null.")
    private Date plannedTime;

    public UUID getDiscountId() {
        return discountId;
    }

    public Date getPlannedTime() {
        return plannedTime;
    }
}
