package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.dtos.ResetCodeDto;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.JsonConverter;

public class ResetCodeActivity extends AppCompatActivity {

    private EditText usernameText;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_code);

        usernameText = findViewById(R.id.resetCodeUsername);
        messageText = findViewById(R.id.resetCodeMessage);

        final Button submit = findViewById(R.id.resetCodeSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendResetCode();
            }
        });

        final Button toLogin = findViewById(R.id.resetCodeReturnToLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openLoginActivity();
            }
        });
    }

    private void sendResetCode() {
        String username = usernameText.getText().toString();
        if(username.length() == 0) {
            messageText.setText(R.string.username_empty_warning);
        }
        else {
            ResetCodeDto resetCodeDto = new ResetCodeDto(username);
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_unsecure_prefix) + "/users/send-reset-code";
            String jsonString = JsonConverter.objectToJson(resetCodeDto);
            try {
                apiCaller.execute("POST", url, null, jsonString);
                openResetPasswordActivity();
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

    private void openResetPasswordActivity() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}
