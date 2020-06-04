package entity;

import java.util.UUID;

public class Settings {

    private UUID cityId;

    private Double basePrice;

    private Double intervalPrice;

    private Integer intervalTime;

    private Boolean discountsUsed;

    private Double discountValue;

    private Boolean transportsUsed;

    public UUID getCityId() {
        return cityId;
    }

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
}
