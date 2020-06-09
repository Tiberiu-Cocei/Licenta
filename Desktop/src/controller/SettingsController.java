package controller;

import entity.AdminLoggedIn;
import entity.City;
import entity.Settings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import utility.ApiCaller;
import utility.ApiResponse;
import utility.JsonConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class SettingsController implements Initializable {

    @FXML
    private ComboBox<String> cityComboBox;

    @FXML
    private Button getButton;

    @FXML
    private TextField id;

    @FXML
    private TextField basePrice;

    @FXML
    private TextField intervalPrice;

    @FXML
    private TextField intervalTime;

    @FXML
    private TextField discountValue;

    @FXML
    private RadioButton discountsUsed;

    @FXML
    private RadioButton transportsUsed;

    @FXML
    private Button updateButton;

    @FXML
    private Text messageText;

    private UUID idSelectedCity;

    private Settings settings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int i = 0;
        for(City city : AdminLoggedIn.getCityList()) {
            cityComboBox.getItems().add(i, city.getName());
            i++;
        }

        addCityComboBoxListener();
        getButton.setOnAction(e -> getSettings(true));
        updateButton.setOnAction(e -> updateSettings());
    }

    private void addCityComboBoxListener() {
        cityComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem != null) {
                int indexCurrentCity = cityComboBox.getSelectionModel().getSelectedIndex();
                idSelectedCity = AdminLoggedIn.getCityList().get(indexCurrentCity).getCityId();
            }
        });
    }

    private void getSettings(boolean showText) {
        if(idSelectedCity == null) {
            return;
        }
        ApiCaller apiCaller = new ApiCaller();
        String url = "http://localhost:8085/api/admin/settings/" + idSelectedCity.toString();
        try {
            AdminLoggedIn adminLoggedIn = AdminLoggedIn.getAdminLoggedIn();
            ApiResponse apiResponse =
                    apiCaller.callApi("GET", url, adminLoggedIn.getAuthenticationToken().toString(), null);
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        if(showText) {
                            messageText.setText("Successfully retrieved settings for city.");
                        }
                        settings = Settings.createSettingsFromJson(apiResponse.getJson());
                        showSettings();
                    } catch(Exception e) {
                        messageText.setText("Failed to create settings object from JSON.");
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(Exception e) {
            messageText.setText("Failed to call API.");
            e.printStackTrace();
        }
    }

    private void showSettings() {
        id.setText(settings.getCityId().toString());
        basePrice.setText(settings.getBasePrice().toString());
        intervalPrice.setText(settings.getIntervalPrice().toString());
        intervalTime.setText(settings.getIntervalTime().toString());
        discountValue.setText(settings.getDiscountValue().toString());
        discountsUsed.setSelected(settings.getDiscountsUsed());
        transportsUsed.setSelected(settings.getTransportsUsed());
    }

    private void updateSettings() {
        boolean areInputsValid = verifyInputs();
        if(areInputsValid) {
            ApiCaller apiCaller = new ApiCaller();
            String url = "http://localhost:8085/api/admin/settings/";
            String jsonString = JsonConverter.objectToJson(settings);
            try {
                ApiResponse apiResponse = apiCaller.callApi(
                        "POST", url, AdminLoggedIn.getAdminLoggedIn().getAuthenticationToken().toString(), jsonString);
                messageText.setText(apiResponse.getJson());

                if(apiResponse.getJson().equals("Successfully updated city settings.")) {
                    getSettings(false);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                messageText.setText("Failed to call API.");
            }
        }
    }

    private boolean verifyInputs() {
        try {
            Double basePriceVal = Double.valueOf(basePrice.getText());
            settings.setBasePrice(basePriceVal);
        } catch(Exception e) {
            messageText.setText("Invalid value for base price.");
            return false;
        }

        try {
            Double intervalPriceVal = Double.valueOf(intervalPrice.getText());
            settings.setIntervalPrice(intervalPriceVal);
        } catch(Exception e) {
            messageText.setText("Invalid value for interval price.");
            return false;
        }

        try {
            Integer intervalTimeVal = Integer.valueOf(intervalTime.getText());
            settings.setIntervalTime(intervalTimeVal);
        } catch(Exception e) {
            messageText.setText("Invalid value for interval time.");
            return false;
        }

        try {
            Double discountValueVal = Double.valueOf(discountValue.getText());
            settings.setDiscountValue(discountValueVal);
        } catch(Exception e) {
            messageText.setText("Invalid value for discount value");
            return false;
        }

        settings.setDiscountsUsed(discountsUsed.isSelected());
        settings.setTransportsUsed(transportsUsed.isSelected());
        return true;
    }
}
