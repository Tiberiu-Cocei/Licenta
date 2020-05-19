package com.thesis.webapi.dtos;

import java.util.Date;

public class AppTransactionHistoryDto {

    private String startStation;

    private String plannedStation;

    private String finishStation;

    private Double discountValue;

    private Date startTime;

    private Date plannedTime;

    private Date finishTime;

    private Double initialCost;

    private Double penalty;

    private AppTransactionHistoryDto() {}

    public static class Builder {

        private String startStation;

        private String plannedStation;

        private String finishStation;

        private Double discountValue;

        private Date startTime;

        private Date plannedTime;

        private Date finishTime;

        private Double initialCost;

        private Double penalty;

        public Builder withStartStation(String startStation) {
            this.startStation = startStation;
            return this;
        }

        public Builder withPlannedStation(String plannedStation) {
            this.plannedStation = plannedStation;
            return this;
        }

        public Builder withFinishStation(String finishStation) {
            this.finishStation = finishStation;
            return this;
        }

        public Builder withDiscountValue(Double discountValue) {
            this.discountValue = discountValue;
            return this;
        }

        public Builder withStartTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder withPlannedTime(Date plannedTime) {
            this.plannedTime = plannedTime;
            return this;
        }

        public Builder withFinishTime(Date finishTime) {
            this.finishTime = finishTime;
            return this;
        }

        public Builder withInitialCost(Double initialCost) {
            this.initialCost = initialCost;
            return this;
        }

        public Builder withPenalty(Double penalty) {
            this.penalty = penalty;
            return this;
        }

        private void reset() {
            this.startStation = null;
            this.plannedStation = null;
            this.finishStation = null;
            this.discountValue = null;
            this.startTime = null;
            this.plannedTime = null;
            this.finishTime = null;
            this.initialCost = null;
            this.penalty = null;
        }

        public AppTransactionHistoryDto build() {
            AppTransactionHistoryDto appTransactionHistoryDto = new AppTransactionHistoryDto();
            appTransactionHistoryDto.startStation = this.startStation;
            appTransactionHistoryDto.plannedStation = this.plannedStation;
            appTransactionHistoryDto.finishStation = this.finishStation;
            appTransactionHistoryDto.discountValue = this.discountValue;
            appTransactionHistoryDto.startTime = this.startTime;
            appTransactionHistoryDto.plannedTime = this.plannedTime;
            appTransactionHistoryDto.finishTime = this.finishTime;
            appTransactionHistoryDto.initialCost = this.initialCost;
            appTransactionHistoryDto.penalty = this.penalty;

            reset();

            return appTransactionHistoryDto;
        }
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
