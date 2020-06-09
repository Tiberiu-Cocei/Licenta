package controller;

import entity.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class ActivitiesController implements Initializable {

    @FXML
    private ComboBox<String> cityCombo;

    @FXML
    private ComboBox<String> stationCombo;

    @FXML
    private Button getActivitiesButton;

    @FXML
    private ListView<String> activityListView;

    @FXML
    private ListView<String> predictedActivityListView;

    @FXML
    private Button activityNextPage;

    @FXML
    private Button activityPreviousPage;

    @FXML
    private Text activityPageNumber;

    @FXML
    private Button predictedActivityNextPage;

    @FXML
    private Button predictedActivityPreviousPage;

    @FXML
    private Text predictedActivityPageNumber;

    @FXML
    private Text activityId;

    @FXML
    private Text activityBicyclesTaken;

    @FXML
    private Text activityBicyclesBrought;

    @FXML
    private Text activityDiscountsFrom;

    @FXML
    private Text activityDiscountsTo;

    @FXML
    private Text activityWasFull;

    @FXML
    private Text activityWasEmpty;

    @FXML
    private Text activityClickedFull;

    @FXML
    private Text activityClickedEmpty;

    @FXML
    private Text predictedActivityId;

    @FXML
    private Text predictedActivityNumber;

    private final static int ACTIVITIES_PER_PAGE = 25;

    private UUID idSelectedCity;

    private UUID idSelectedStation;

    private List<Activity> activityList;

    private List<PredictedActivity> predictedActivityList;

    private int currentActivityPage;

    private int currentPredictedActivityPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int i = 0;
        for(City city : AdminLoggedIn.getCityList()) {
            cityCombo.getItems().add(i, city.getName());
            i++;
        }

        activityPageNumber.setText("1");
        predictedActivityPageNumber.setText("1");
        currentActivityPage = 1;
        currentPredictedActivityPage = 1;

        addCityComboBoxListener();
        addStationComboBoxListener();
        getActivitiesButton.setOnAction(e -> addGetActivitiesButtonListener());
        activityListView.getSelectionModel().selectedItemProperty().addListener(e -> showActivityDetails());
        predictedActivityListView.getSelectionModel().selectedItemProperty().addListener(e -> showPredictedActivityDetails());

        activityNextPage.setOnAction(e -> addActivityNextPageListener());
        activityPreviousPage.setOnAction(e -> addActivityPreviousPageListener());
        predictedActivityNextPage.setOnAction(e -> addPredictedActivityNextPageListener());
        predictedActivityPreviousPage.setOnAction(e -> addPredictedActivityPreviousPageListener());
    }

    private void addPredictedActivityNextPageListener() {
        currentPredictedActivityPage++;
        getAndUpdatePredictedActivityListAfterButtonClick();
    }

    private void addPredictedActivityPreviousPageListener() {
        if(currentPredictedActivityPage == 1) {
            return;
        }
        currentPredictedActivityPage--;
        getAndUpdatePredictedActivityListAfterButtonClick();
    }

    private void getAndUpdatePredictedActivityListAfterButtonClick() {
        predictedActivityPageNumber.setText(Integer.valueOf(currentPredictedActivityPage).toString());
        getPredictedActivityList((currentPredictedActivityPage - 1) * ACTIVITIES_PER_PAGE);
        showPredictedActivityList();
    }

    private void addActivityNextPageListener() {
        currentActivityPage++;
        getAndUpdateActivityListAfterButtonClick();
    }

    private void addActivityPreviousPageListener() {
        if(currentActivityPage == 1) {
            return;
        }
        currentActivityPage--;
        getAndUpdateActivityListAfterButtonClick();
    }

    private void getAndUpdateActivityListAfterButtonClick() {
        activityPageNumber.setText(Integer.valueOf(currentActivityPage).toString());
        getActivityList((currentActivityPage - 1) * ACTIVITIES_PER_PAGE);
        showActivityList();
    }

    private void showActivityDetails() {
        int index = activityListView.getSelectionModel().getSelectedIndex();
        if(index < 0) {
            return;
        }

        Activity activity = activityList.get(index);
        activityId.setText(activity.getId().toString());
        activityBicyclesTaken.setText(Integer.valueOf(activity.getBicyclesTaken()).toString());
        activityBicyclesBrought.setText(Integer.valueOf(activity.getBicyclesBrought()).toString());
        activityDiscountsFrom.setText(Integer.valueOf(activity.getDiscountsFrom()).toString());
        activityDiscountsTo.setText(Integer.valueOf(activity.getDiscountsTo()).toString());
        activityWasFull.setText(Boolean.valueOf(activity.isWasStationFull()).toString());
        activityWasEmpty.setText(Boolean.valueOf(activity.isWasStationEmpty()).toString());
        activityClickedFull.setText(Integer.valueOf(activity.getTimesClickedWhileFull()).toString());
        activityClickedEmpty.setText(Integer.valueOf(activity.getTimesClickedWhileEmpty()).toString());
    }

    private void showPredictedActivityDetails() {
        int index = predictedActivityListView.getSelectionModel().getSelectedIndex();
        if(index < 0) {
            return;
        }

        PredictedActivity predictedActivity = predictedActivityList.get(index);
        predictedActivityId.setText(predictedActivity.getId().toString());
        predictedActivityNumber.setText(Integer.valueOf(predictedActivity.getNumberOfBicycles()).toString());
    }

    private void addGetActivitiesButtonListener() {
        if(idSelectedStation != null) {
            activityListView.getItems().clear();
            predictedActivityListView.getItems().clear();
            getActivityList(0);
            getPredictedActivityList(0);
            showActivityList();
            showPredictedActivityList();
        }
    }

    private void showActivityList() {
        activityListView.getItems().clear();
        String datePattern = "MM/dd/yyyy";
        DateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.US);
        if(activityList != null) {
            int i = 0;
            for(Activity activity : activityList) {
                String date = dateFormat.format(activity.getDay());
                activityListView.getItems().add(i, date + " " + activity.getHourFrom() + ":00");
                i++;
            }
        }
    }

    private void showPredictedActivityList() {
        predictedActivityListView.getItems().clear();
        String datePattern = "MM/dd/yyyy";
        DateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.US);
        if(predictedActivityList != null) {
            int i = 0;
            for(PredictedActivity predictedActivity : predictedActivityList) {
                String date = dateFormat.format(predictedActivity.getDay());
                activityListView.getItems().add(i, date + " " + predictedActivity.getHour() + ":00");
                i++;
            }
        }
    }

    private void getActivityList(int offset) {
        if(idSelectedStation == null) {
            return;
        }
        ApiCaller apiCaller = new ApiCaller();
        String url = "http://localhost:8085/api/admin/activities/?limit=" +
                ACTIVITIES_PER_PAGE + "&offset=" + offset + "&id=" + idSelectedStation.toString();
        try {
            AdminLoggedIn adminLoggedIn = AdminLoggedIn.getAdminLoggedIn();
            ApiResponse apiResponse =
                    apiCaller.callApi("GET", url, adminLoggedIn.getAuthenticationToken().toString(), null);
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        activityList = Activity.createActivityListFromJson(apiResponse.getJson());
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void getPredictedActivityList(int offset) {
        if(idSelectedStation == null) {
            return;
        }
        ApiCaller apiCaller = new ApiCaller();
        String url = "http://localhost:8085/api/admin/predicted-activities/?limit=" +
                ACTIVITIES_PER_PAGE + "&offset=" + offset + "&id=" + idSelectedStation.toString();
        try {
            AdminLoggedIn adminLoggedIn = AdminLoggedIn.getAdminLoggedIn();
            ApiResponse apiResponse =
                    apiCaller.callApi("GET", url, adminLoggedIn.getAuthenticationToken().toString(), null);
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        predictedActivityList = PredictedActivity.createPredictedActivityListFromJson(apiResponse.getJson());
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addCityComboBoxListener() {
        cityCombo.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem != null) {
                int indexCurrentCity = cityCombo.getSelectionModel().getSelectedIndex();
                idSelectedCity = AdminLoggedIn.getCityList().get(indexCurrentCity).getCityId();
                stationCombo.getItems().clear();
                List<StationInfo> stationInfoList = AdminLoggedIn.getStationLists().get(idSelectedCity);
                int i = 0;
                for(StationInfo stationInfo : stationInfoList) {
                    stationCombo.getItems().add(i, stationInfo.getName());
                    i++;
                }
            }
        });
    }

    private void addStationComboBoxListener() {
        stationCombo.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem != null && !stationCombo.getSelectionModel().isEmpty()) {
                int indexCurrentStation = stationCombo.getSelectionModel().getSelectedIndex();
                List<StationInfo> stationInfoList = AdminLoggedIn.getStationLists().get(idSelectedCity);
                idSelectedStation = stationInfoList.get(indexCurrentStation).getId();
            }
        });
    }
}
