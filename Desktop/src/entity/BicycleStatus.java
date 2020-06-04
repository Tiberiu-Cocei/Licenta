package entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class BicycleStatus {

    private int stationNumber;

    private int warehouseNumber;

    private int transportNumber;

    private int userNumber;

    private int damagedNumber;

    private int stolenNumber;

    public static List<BicycleStatus> createBicycleStatusListFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<BicycleStatus>>(){});
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public int getWarehouseNumber() {
        return warehouseNumber;
    }

    public int getTransportNumber() {
        return transportNumber;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public int getDamagedNumber() {
        return damagedNumber;
    }

    public int getStolenNumber() {
        return stolenNumber;
    }
}
