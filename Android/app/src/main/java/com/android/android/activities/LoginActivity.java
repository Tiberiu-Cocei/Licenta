package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.dtos.LoginDto;
import com.android.android.entities.AppDetails;
import com.android.android.entities.City;
import com.android.android.entities.Station;
import com.android.android.entities.User;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.JsonConverter;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText;

    private EditText passwordText;

    private TextView messageText;

    private TextView citySelection;

    private City selectedCity = null;

    private AppDetails appDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.loginUsername);
        passwordText = findViewById(R.id.loginPassword);
        messageText = findViewById(R.id.loginMessage);
        citySelection = findViewById(R.id.loginCityChoice);
        appDetails = AppDetails.getAppDetails();

        createAndListenToCitySpinner();

        final Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptLogin();
            }
        });

        final Button toRegister = findViewById(R.id.loginToRegister);
        toRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        final Button toReset = findViewById(R.id.loginToReset);
        toReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openResetCodeActivity();
            }
        });
    }

    private void openResetCodeActivity() {
        Intent intent = new Intent(this, ResetCodeActivity.class);
        startActivity(intent);
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    private void createAndListenToCitySpinner() {
        final List<City> cityList = getAllCities();
        if(cityList != null) {
            selectedCity = cityList.get(0);
            ArrayList<String> cityNames = new ArrayList<>();
            for(City city : cityList) {
                cityNames.add(city.getName());
            }
            Spinner cityNameSpinner = findViewById(R.id.loginCitySpinner);
            ArrayAdapter<String> cityNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityNames);
            cityNameSpinner.setAdapter(cityNameAdapter);

            cityNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    selectedCity = cityList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {}
            });
        }
        else {
            citySelection.setText(getResources().getString(R.string.login_city_api_error));
        }
    }

    private List<City> getAllCities() {
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(R.string.api_unsecure_prefix) + "/cities";
        try {
            apiCaller.execute("GET", url, null, null);
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    return City.createCityListFromJson(apiResponse.getJson());
                }
            }
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void attemptLogin() {
        if(selectedCity == null) {
            messageText.setText(getResources().getString(R.string.login_button_with_no_selected_city));
            return;
        }
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        if(username.length() == 0) {
            messageText.setText(R.string.username_empty_warning);
        }
        else if(password.length() == 0) {
            messageText.setText(R.string.password_empty_warning);
        }
        else {
            LoginDto loginDto = new LoginDto(username, password);
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_unsecure_prefix) + "/users/login";
            String jsonString = JsonConverter.objectToJson(loginDto);
            try {
                apiCaller.execute("POST", url, null, jsonString);
                ApiResponse apiResponse = apiCaller.get();
                if(apiResponse != null) {
                    if(apiResponse.getCode() == 200) {
                        try {
                            User.createUserFromJson(apiResponse.getJson());
                            appDetails.setCity(selectedCity);
                            List<Station> stationList = getStationsForSelectedCity();
                            if(stationList != null) {
                                appDetails.setStationList(stationList);
                                messageText.setText(getResources().getString(R.string.api_successful_login));
                                openMapActivity();
                            }
                            else {
                                messageText.setText(getResources().getString(R.string.login_station_api_error));
                            }
                        } catch(Exception e) {
                            messageText.setText(getResources().getString(R.string.api_failed_json_to_details));
                        }
                    }
                    else {
                        messageText.setText(getResources().getString(R.string.api_failed_login));
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                messageText.setText(getResources().getString(R.string.api_generic_call_failure));
            }
        }
    }

    private List<Station> getStationsForSelectedCity() {
        List<Station> stationList = null;
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(R.string.api_secure_prefix) + "/stations/city";
        try {
            apiCaller.execute("GET", url, User.getUser().getAuthenticationToken().toString(),
                    appDetails.getCity().getCityId().toString());
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        stationList = Station.createStationListFromJson(apiResponse.getJson());
                    } catch(Exception e) {
                        messageText.setText(getResources().getString(R.string.api_failed_json_to_details));
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            messageText.setText(getResources().getString(R.string.api_generic_call_failure));
        }
        return stationList;
    }
}
