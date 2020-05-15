package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.dtos.LoginDto;
import com.android.android.entities.User;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.JsonConverter;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText;

    private EditText passwordText;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.loginUsername);
        passwordText = findViewById(R.id.loginPassword);
        messageText = findViewById(R.id.loginMessage);

        final Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
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
                            User.createUser(apiResponse.getJson());
                            messageText.setText(getResources().getString(R.string.api_successful_login));
                            //TODO: intent for "main menu"
                        } catch(Exception e) {
                            messageText.setText(getResources().getString(R.string.api_failed_json_to_user_object));
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
}
