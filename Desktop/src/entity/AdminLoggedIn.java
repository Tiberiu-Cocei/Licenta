package entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class AdminLoggedIn {

    private static AdminLoggedIn adminLoggedIn;

    private static List<City> cityList;

    private static HashMap<UUID, List<StationInfo>> stationLists;

    private UUID staffId;

    private String username;

    private UUID authenticationToken;

    public static void createLoggedAdminFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        adminLoggedIn = objectMapper.readValue(json, AdminLoggedIn.class);
    }

    public static AdminLoggedIn getAdminLoggedIn() {
        if(adminLoggedIn == null) {
            adminLoggedIn = new AdminLoggedIn();
        }
        return adminLoggedIn;
    }

    public static List<City> getCityList() {
        return cityList;
    }

    public static void setCityList(List<City> cityList) {
        AdminLoggedIn.cityList = cityList;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(UUID authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public static HashMap<UUID, List<StationInfo>> getStationLists() {
        return stationLists;
    }

    public static void setStationLists(HashMap<UUID, List<StationInfo>> stationLists) {
        AdminLoggedIn.stationLists = stationLists;
    }
}
