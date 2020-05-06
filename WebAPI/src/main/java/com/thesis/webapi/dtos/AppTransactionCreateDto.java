package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

public class AppTransactionCreateDto {

    @NotNull(message = "Payment method id cannot be null.")
    private UUID paymentMethodId;

    @NotNull(message = "User id cannot be null.")
    private UUID userId;

    @NotNull(message = "Bicycle id cannot be null.")
    private UUID bicycleId;

    @NotNull(message = "Start station id cannot be null.")
    private UUID startStationId;

    @NotNull(message = "Planned station id cannot be null.")
    private UUID plannedStationId;

    private UUID discountId;

    @NotNull(message = "Planned time cannot be null.")
    private Date plannedTime;

    @NotNull(message = "Card security code cannot be null.")
    private String cardSecurityCode;

    @NotNull(message = "City id cannot be null.")
    private UUID cityId;

    public AppTransactionCreateDto() {}

    public AppTransactionCreateDto(UUID userId, UUID startStationId, UUID plannedStationId, Date plannedTime, UUID cityId, UUID bicycleId) {
        this.userId = userId;
        this.startStationId = startStationId;
        this.plannedStationId = plannedStationId;
        this.plannedTime = plannedTime;
        this.cityId = cityId;
        this.bicycleId = bicycleId;
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
