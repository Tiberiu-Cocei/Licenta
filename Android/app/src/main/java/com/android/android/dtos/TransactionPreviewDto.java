package com.android.android.dtos;

import com.android.android.entities.AppDetails;

import java.util.Date;
import java.util.UUID;

public class TransactionPreviewDto {

    private UUID discountId;

    private Date plannedTime;

    private UUID cityId;

    public TransactionPreviewDto(Date plannedTime) {
        AppDetails appDetails = AppDetails.getAppDetails();
        if(appDetails.getDiscount() != null) {
            this.discountId = AppDetails.getAppDetails().getDiscount().getId();
        }
        else {
            this.discountId = null;
        }
        this.plannedTime = plannedTime;
        this.cityId = AppDetails.getAppDetails().getCity().getCityId();
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
