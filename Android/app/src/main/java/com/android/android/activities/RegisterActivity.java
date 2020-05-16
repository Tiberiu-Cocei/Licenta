package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.dtos.RegisterDto;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.JsonConverter;

import org.apache.commons.validator.routines.EmailValidator;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameText;

    private EditText emailText;

    private EditText passwordText;

    private EditText confirmPasswordText;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText = findViewById(R.id.registerUsername);
        emailText = findViewById(R.id.registerEmail);
        passwordText = findViewById(R.id.registerPassword);
        messageText = findViewById(R.id.registerMessage);
        confirmPasswordText = findViewById(R.id.registerConfirmPassword);

        final Button register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptRegister();
            }
        });

        final Button toLogin = findViewById(R.id.registerToLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openLoginActivity();
            }
        });
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void attemptRegister() {
        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();
        boolean isEmailValid = EmailValidator.getInstance().isValid(email);
        if(username.length() < 5 || username.length() > 30) {
            messageText.setText(R.string.username_length_warning);
        }
        else if (!isEmailValid) {
            messageText.setText(R.string.email_invalid_warning);
        }
        else if(password.length() < 8 || password.length() > 50) {
            messageText.setText(R.string.password_length_warning);
        }
        else if(!password.equals(confirmPassword)) {
            messageText.setText(R.string.passwords_do_not_match);
        }
        else {
            RegisterDto registerDto = new RegisterDto(email, username, password);
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_unsecure_prefix) + "/users/register";
            String jsonString = JsonConverter.objectToJson(registerDto);
            try {
                apiCaller.execute("POST", url, null, jsonString);
                ApiResponse apiResponse = apiCaller.get();
                messageText.setText(apiResponse.getJson());
            }
            catch(Exception e) {
                e.printStackTrace();
                messageText.setText(getResources().getString(R.string.api_generic_call_failure));
            }
        }
    }
}
