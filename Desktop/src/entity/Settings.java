package entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class Settings {

    private UUID cityId;

    private Double basePrice;

    private Double intervalPrice;

    private Integer intervalTime;

    private Boolean discountsUsed;

    private Double discountValue;

    private Boolean transportsUsed;

    public static Settings createSettingsFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, Settings.class);
    }

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

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public void setIntervalPrice(Double intervalPrice) {
        this.intervalPrice = intervalPrice;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public void setDiscountsUsed(Boolean discountsUsed) {
        this.discountsUsed = discountsUsed;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public void setTransportsUsed(Boolean transportsUsed) {
        this.transportsUsed = transportsUsed;
    }
}
