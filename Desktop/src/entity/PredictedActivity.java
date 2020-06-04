package entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PredictedActivity {

    private UUID id;

    private UUID stationId;

    private Date day;

    private int hour;

    private int numberOfBicycles;

    public static List<PredictedActivity> createPredictedActivityListFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<PredictedActivity>>(){});
    }

    public UUID getId() {
        return id;
    }

    public UUID getStationId() {
        return stationId;
    }

    public Date getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getNumberOfBicycles() {
        return numberOfBicycles;
    }
}
