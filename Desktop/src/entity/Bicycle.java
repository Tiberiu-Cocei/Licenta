package entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Bicycle {

    private UUID id;

    private UUID stationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS")
    private Date arrivalTime;

    private String status;

    private String model;

    private Integer lockNumber;

    public static List<Bicycle> createBicycleListFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<Bicycle>>(){});
    }

    public UUID getId() {
        return id;
    }

    public UUID getStationId() {
        return stationId;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public String getModel() {
        return model;
    }

    public Integer getLockNumber() {
        return lockNumber;
    }
}
