package com.mad.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    TextView signupText;
    Button loginButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID",-1);
        if (userId != -1) {
            // User is logged in, navigate to QuizActivity
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.userNameText);
        password = findViewById(R.id.userPasswordText);
        signupText = findViewById(R.id.textSignup);
        loginButton = findViewById(R.id.loginButton);
        databaseHelper = new DatabaseHelper(this);

        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> loginValidation());
    }
    // Toast message
    private void toastText (String text){
        Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
    }

    private void loginValidation() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            toastText("Please enter username and password");
        } else {
            int userId = databaseHelper.checkLogin(user, pass);
            if (userId != -1) {
                // Retrieve user ID and store in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("USER_ID", userId);
                editor.putString("USERNAME", user);
                editor.apply();

                // Navigate to MainMenuActivity
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                toastText("Invalid username or password");
            }
        }
    }
}