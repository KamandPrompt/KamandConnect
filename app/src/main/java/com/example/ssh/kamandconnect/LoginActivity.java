package com.example.ssh.kamandconnect;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEmailEditText;
    private EditText mLoginPasswordEditText;

    private Button mLoginButton;
    private Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginEmailEditText = (EditText) findViewById(R.id.login_email_edit_text);
        mLoginPasswordEditText = (EditText) findViewById(R.id.login_password_edit_text);

        mLoginButton = (Button) findViewById(R.id.button_login);
        mSignupButton = (Button) findViewById(R.id.button_signup);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
