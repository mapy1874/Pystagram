package com.example.pystagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public final static String TAG = "LoginActivity";
    private EditText etPassword;
    private EditText etUsername;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            goMainActivity();
        } else {
            // show the signup or login screen
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            etPassword = findViewById(R.id.etPassword);
            etUsername = findViewById(R.id.etUsername);
            btnLogin = findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    login(username, password);
                }
            });
        }

    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // TODO: Better error handling
                    CharSequence text = "Either your username or password is wrong, please try again!";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    Log.e(TAG,"Issue with login" );
                    e.printStackTrace();
                    return;
                }
                // TODO: Navigate to the new activity if the user signed properly
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Log.d(TAG,"Navigating to MainActivity" );

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
