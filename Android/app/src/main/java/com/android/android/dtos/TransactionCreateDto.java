package com.android.android.dtos;

import com.android.android.entities.AppDetails;
import com.android.android.entities.User;

import java.util.Date;
import java.util.UUID;

public class TransactionCreateDto {

    private UUID paymentMethodId;

    private UUID userId;

    private UUID bicycleId;

    private UUID startStationId;

    private UUID plannedStationId;

    private UUID discountId;

    private Date plannedTime;

    private String cardSecurityCode;

    private UUID cityId;

    public TransactionCreateDto(UUID plannedStationId, UUID discountId, Date plannedTime, String cardSecurityCode) {
        User user = User.getUser();
        AppDetails appDetails = AppDetails.getAppDetails();
        this.paymentMethodId = user.getPaymentMethodId();
        this.userId = user.getId();
        this.bicycleId = appDetails.getBicycleId();
        this.startStationId = appDetails.getStartStationId();
        this.plannedStationId = plannedStationId;
        this.discountId = discountId;
        this.plannedTime = plannedTime;
        this.cardSecurityCode = cardSecurityCode;
        this.cityId = appDetails.getCity().getCityId();
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getBicycleId() {
        return bicycleId;
    }

    public UUID getStartStationId() {
        return startStationId;
    }

    public UUID getPlannedStationId() {
        return plannedStationId;
    }

    public UUID getDiscountId() {
        return discountId;
    }

    public Date getPlannedTime() {
        return plannedTime;
    }

    public String getCardSecurityCode() {
        return cardSecurityCode;
    }

    public UUID getCityId() {
        return cityId;
    }
}
