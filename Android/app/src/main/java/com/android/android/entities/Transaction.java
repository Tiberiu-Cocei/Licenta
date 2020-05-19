package com.android.android.entities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;

public class Transaction {

    private String startStation;

    private String plannedStation;

    private String finishStation;

    private Double discountValue;

    private Date startTime;

    private Date plannedTime;

    private Date finishTime;

    private Double initialCost;

    private Double penalty;

    public static List<Transaction> createTransactionListFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<Transaction>>(){});
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getPlannedStation() {
        return plannedStation;
    }

    public void setPlannedStation(String plannedStation) {
        this.plannedStation = plannedStation;
    }

    public String getFinishStation() {
        return finishStation;
    }

    public void setFinishStation(String finishStation) {
        this.finishStation = finishStation;
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

    public Date getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(Date plannedTime) {
        this.plannedTime = plannedTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Double getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(Double initialCost) {
        this.initialCost = initialCost;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }
}
