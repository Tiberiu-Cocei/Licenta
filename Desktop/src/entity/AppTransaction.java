package entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AppTransaction {

    private UUID id;

    private UUID paymentMethodId;

    private UUID userId;

    private UUID bicycleId;

    private UUID startStationId;

    private UUID plannedStationId;

    private UUID finishStationId;

    private UUID discountId;

    private Date startTime;

    private Date plannedTime;

    private Date finishTime;

    private Double initialCost;

    private Double penalty;

    public static List<AppTransaction> createAppTransactionListFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<AppTransaction>>(){});
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
}
