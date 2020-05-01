package com.thesis.webapi.entities;

import com.thesis.webapi.dtos.AppTransactionCreateDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "app_transaction")
public class AppTransaction {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "payment_method_id")
    private UUID paymentMethodId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "bicycle_id")
    private UUID bicycleId;

    @Column(name = "start_station_id")
    private UUID startStationId;

    @Column(name = "planned_station_id")
    private UUID plannedStationId;

    @Column(name = "finish_station_id")
    private UUID finishStationId;

    @Column(name = "discount_id")
    private UUID discountId;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "planned_time")
    private Date plannedTime;

    @Column(name = "finish_time")
    private Date finishTime;

    @Column(name = "initial_cost")
    private Double initialCost;

    @Column(name = "penalty")
    private Double penalty;

    public AppTransaction() {}

    public AppTransaction(AppTransactionCreateDto appTransactionCreateDto) {
        this.id = UUID.randomUUID();
        this.paymentMethodId = appTransactionCreateDto.getPaymentMethodId();
        this.userId = appTransactionCreateDto.getUserId();
        this.bicycleId = appTransactionCreateDto.getBicycleId();
        this.startStationId = appTransactionCreateDto.getStartStationId();
        this.plannedStationId = appTransactionCreateDto.getPlannedStationId();
        this.finishStationId = null;
        this.discountId = appTransactionCreateDto.getDiscountId();
        this.startTime = new Date();
        this.plannedTime = appTransactionCreateDto.getPlannedTime();
        this.finishTime = null;
        this.initialCost = null;
        this.penalty = null;
    }

    public UUID getId() {
        return id;
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

    public UUID getFinishStationId() {
        return finishStationId;
    }

    public UUID getDiscountId() {
        return discountId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getPlannedTime() {
        return plannedTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public Double getInitialCost() {
        return initialCost;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setFinishStationId(UUID finishStationId) {
        this.finishStationId = finishStationId;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public void setInitialCost(Double initialCost) {
        this.initialCost = initialCost;
    }
}
