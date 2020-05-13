package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.utilities.ApiCaller;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText;

    private EditText passwordText;

    private TextView messageText;

    private ApiCaller apiCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.loginUsername);
        passwordText = findViewById(R.id.loginPassword);
        messageText = findViewById(R.id.loginMessage);
        apiCaller = new ApiCaller();

        final Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                if(username.length() == 0) {
                    messageText.setText(R.string.username_empty_warning);
                }
                else if(password.length() == 0) {
                    messageText.setText(R.string.password_empty_warning);
                }
                else {
                    String url = getResources().getString(R.string.api_unsecure_prefix) + "/users/login";
                    String errorMessage = getResources().getString(R.string.api_generic_call_failure);
                    String jsonString = "{\"username\":" + "\"" + username + "\", \"password\":" + "\"" + password + "\"}";
                    try {
                        apiCaller.execute("POST", url, errorMessage, jsonString);
                        messageText.setText(apiCaller.get());
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        messageText.setText(errorMessage);
                    }
                }
            }
        });
    }
}
