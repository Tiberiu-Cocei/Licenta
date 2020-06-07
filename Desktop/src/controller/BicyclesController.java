package controller;

import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import utility.ApiCaller;
import utility.ApiResponse;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class BicyclesController implements Initializable {

    @FXML
    private ComboBox<String> cityComboBox;

    @FXML
    private ComboBox<String> stationComboBox;

    @FXML
    private Button getBicycleListButton;

    @FXML
    private PieChart bicycleStatusChart;

    @FXML
    private ListView<String> bicycleListView;

    @FXML
    private Text bicycleId;

    @FXML
    private Text arrivalTime;

    @FXML
    private Text status;

    @FXML
    private Text model;

    @FXML
    private Text lockNumber;

    @FXML
    private Text messageText;

    private UUID idSelectedCity;

    private UUID idSelectedStation;

    private List<Bicycle> bicycleList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int i = 0;
        for(City city : AdminLoggedIn.getCityList()) {
            cityComboBox.getItems().add(i, city.getName());
            i++;
        }

        BicycleStatus bicycleStatus = getBicycleStatus(null);
        updatePieChart(bicycleStatus);
        addCityComboBoxListener();
        addStationComboBoxListener();
        getBicycleListButton.setOnAction(e -> addGetBicycleListButtonListener());
        bicycleListView.getSelectionModel().selectedItemProperty().addListener(e -> showDetails());
    }

    private void showDetails() {
        int index = bicycleListView.getSelectionModel().getSelectedIndex();
        if(index < 0) {
            return;
        }
        Bicycle bicycle = bicycleList.get(index);
        bicycleId.setText(bicycle.getId().toString());

        if(bicycle.getArrivalTime() != null) {
            String datePattern = "HH:mm:ss";
            DateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.US);
            arrivalTime.setText(dateFormat.format(bicycle.getArrivalTime()));
        }
        else {
            arrivalTime.setText("N/A");
        }

        status.setText(bicycle.getStatus());
        model.setText(bicycle.getModel());

        if(bicycle.getLockNumber() != null) {
            lockNumber.setText(bicycle.getLockNumber().toString());
        }
        else {
            lockNumber.setText("N/A");
        }
    }

    private void addCityComboBoxListener() {
        cityComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem != null) {
                int indexCurrentCity = cityComboBox.getSelectionModel().getSelectedIndex();
                idSelectedCity = AdminLoggedIn.getCityList().get(indexCurrentCity).getCityId();
                stationComboBox.getItems().clear();
                List<StationInfo> stationInfoList = AdminLoggedIn.getStationLists().get(idSelectedCity);
                int i = 0;
                for(StationInfo stationInfo : stationInfoList) {
                    stationComboBox.getItems().add(i, stationInfo.getName());
                    i++;
                }
            }
        });
    }

    private void addStationComboBoxListener() {
        stationComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem != null && !stationComboBox.getSelectionModel().isEmpty()) {
                int indexCurrentStation = stationComboBox.getSelectionModel().getSelectedIndex();
                List<StationInfo> stationInfoList = AdminLoggedIn.getStationLists().get(idSelectedCity);
                idSelectedStation = stationInfoList.get(indexCurrentStation).getId();
            }
        });
    }

    private void addGetBicycleListButtonListener() {
        if(idSelectedStation != null) {
            bicycleListView.getItems().clear();
            getBicycleList();
            if(bicycleList != null) {
                int i = 0;
                for(Bicycle bicycle : bicycleList) {
                    bicycleListView.getItems().add(i, bicycle.getId().toString());
                    i++;
                }
                BicycleStatus bicycleStatus = getBicycleStatus(idSelectedStation);
                updatePieChart(bicycleStatus);
            }
        }
    }

    private void addNextPageButtonListener() {
        if(bicycleList != null) {
            bicycleListView.getItems().clear();
            getBicycleList();
            if(bicycleList != null) {
                int i = 0;
                for(Bicycle bicycle : bicycleList) {
                    bicycleListView.getItems().add(i, bicycle.getId().toString());
                    i++;
                }
            }
        }
    }

    private void updatePieChart(BicycleStatus bicycleStatus) {
        ObservableList<PieChart.Data> pieChartData =
            FXCollections.observableArrayList(
                new PieChart.Data("Station", bicycleStatus.getStationNumber()),
                new PieChart.Data("Warehouse", bicycleStatus.getWarehouseNumber()),
                new PieChart.Data("User", bicycleStatus.getUserNumber()),
                new PieChart.Data("Transport", bicycleStatus.getTransportNumber()),
                new PieChart.Data("Stolen", bicycleStatus.getStolenNumber()),
                new PieChart.Data("Damaged", bicycleStatus.getDamagedNumber()));

        bicycleStatusChart.setData(pieChartData);
    }

    private BicycleStatus getBicycleStatus(UUID stationId) {
        ApiCaller apiCaller = new ApiCaller();
        BicycleStatus bicycleStatus = null;
        String url = "http://localhost:8085/api/admin/bicycles/status-numbers/";
        if(stationId != null) {
            url += "?id=" + stationId.toString();
        }
        try {
            AdminLoggedIn adminLoggedIn = AdminLoggedIn.getAdminLoggedIn();
            ApiResponse apiResponse =
                    apiCaller.callApi("GET", url, adminLoggedIn.getAuthenticationToken().toString(), null);
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        bicycleStatus = BicycleStatus.createBicycleStatusFromJson(apiResponse.getJson());
                    } catch(Exception e) {
                        e.printStackTrace();
                        messageText.setText("Failed to create bicycle status from JSON");
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            messageText.setText("Failed to call get bicycle status API.");
        }
        return bicycleStatus;
    }

    private void getBicycleList() {
        if(idSelectedStation == null) {
            return;
        }
        ApiCaller apiCaller = new ApiCaller();
        String url = "http://localhost:8085/api/admin/bicycles/?limit=" + 1000 + "&offset=" + 0 + "&id=" + idSelectedStation.toString();
        try {
            AdminLoggedIn adminLoggedIn = AdminLoggedIn.getAdminLoggedIn();
            ApiResponse apiResponse =
                    apiCaller.callApi("GET", url, adminLoggedIn.getAuthenticationToken().toString(), null);
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        bicycleList = Bicycle.createBicycleListFromJson(apiResponse.getJson());
                    } catch(Exception e) {
                        e.printStackTrace();
                        messageText.setText("Failed to create bicycle list from JSON");
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            messageText.setText("Failed to call get bicycles API.");
        }
    }

}
