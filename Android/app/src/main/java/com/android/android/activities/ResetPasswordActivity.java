package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.dtos.ResetPasswordDto;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.JsonConverter;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText usernameText;

    private EditText resetCodeText;

    private EditText passwordText;

    private EditText confirmPasswordText;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        usernameText = findViewById(R.id.resetPasswordUsername);
        resetCodeText = findViewById(R.id.resetPasswordCode);
        passwordText = findViewById(R.id.resetPasswordPassword);
        confirmPasswordText = findViewById(R.id.resetPasswordConfirmPassword);
        messageText = findViewById(R.id.resetPasswordMessage);

        final Button submit = findViewById(R.id.resetPasswordSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptResetPassword();
            }
        });

        final Button toLogin = findViewById(R.id.resetPasswordReturnToLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openLoginActivity();
            }
        });
    }

    private void attemptResetPassword() {
        String username = usernameText.getText().toString();
        String resetCode = resetCodeText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();
        if(username.length() == 0) {
            messageText.setText(R.string.username_empty_warning);
        }
        else if(password.length() < 8 || password.length() > 50) {
            messageText.setText(R.string.password_length_warning);
        }
        else if(!password.equals(confirmPassword)) {
            messageText.setText(R.string.passwords_do_not_match);
        }
        else {
            ResetPasswordDto resetPasswordDto = new ResetPasswordDto(username, resetCode, password);
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_unsecure_prefix) + "/users/reset-password";
            String jsonString = JsonConverter.objectToJson(resetPasswordDto);
            try {
                apiCaller.execute("POST", url, null, jsonString);
                ApiResponse apiResponse = apiCaller.get();
                if(apiResponse.getJson() != null) {
                    messageText.setText(apiResponse.getJson());
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                messageText.setText(getResources().getString(R.string.api_generic_call_failure));
            }
        }
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
