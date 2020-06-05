package controller;

import dto.LoginDto;
import entity.AdminLoggedIn;
import entity.City;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utility.ApiCaller;
import utility.ApiResponse;
import utility.JsonConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane loginPane;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Text loginText;

    @FXML
    private Text messageText;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(e -> attemptLogin());
    }

    private void attemptLogin() {
        String username = usernameText.getText();
        String password = passwordText.getText();
        if(username.length() == 0 || password.length() == 0) {
            messageText.setText("Username and password fields cannot be null");
        }
        else {
            LoginDto loginDto = new LoginDto(username, password);
            ApiCaller apiCaller = new ApiCaller();
            String url = "http://localhost:8085/api/unsecure/admins/login";
            String jsonString = JsonConverter.objectToJson(loginDto);
            try {
                ApiResponse apiResponse = apiCaller.callApi("POST", url, null, jsonString);
                if(apiResponse != null) {
                    if(apiResponse.getCode() == 200) {
                        try {
                            AdminLoggedIn.createLoggedAdminFromJson(apiResponse.getJson());
                            if(AdminLoggedIn.getAdminLoggedIn() != null) {
                                boolean success = getCities();
                                if(success) {
                                    openAdminPanel();
                                }
                            }
                        } catch(Exception e) {
                            messageText.setText("Failed to create logged in admin from JSON.");
                        }
                    }
                    else {
                        messageText.setText("Wrong username or password.");
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                messageText.setText("Failed to call login API.");
            }
        }
    }

    private boolean getCities() {
        ApiCaller apiCaller = new ApiCaller();
        String url = "http://localhost:8085/api/unsecure/cities";
        try {
            AdminLoggedIn adminLoggedIn = AdminLoggedIn.getAdminLoggedIn();
            ApiResponse apiResponse =
                    apiCaller.callApi("GET", url, adminLoggedIn.getAuthenticationToken().toString(), null);
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        AdminLoggedIn.setCityList(City.createCityListFromJson(apiResponse.getJson()));
                        messageText.setText("Successfully logged in.");
                        return true;
                    } catch(Exception e) {
                        messageText.setText("Failed to create city list from JSON");
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            messageText.setText("Failed to call get all cities API.");
        }
        return false;
    }

    private void openAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/view/test.fxml")); //TODO: change with actual fxml
            AnchorPane login = loader.load();
            Scene scene = new Scene(login); //TODO : de vazut daca rezolutia si resize-ul sunt cum trebuie
            stage.setScene(scene);
        } catch(Exception e) {
            e.printStackTrace();
            messageText.setText("Failed to open admin panel.");
        }
    }
}
