package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.dtos.LoginDto;

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
                    //call api
                }
            }
        });
    }
}
