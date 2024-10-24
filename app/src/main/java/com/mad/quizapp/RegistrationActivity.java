package com.mad.quizapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrationActivity extends AppCompatActivity {
    EditText username, password, confirmPassword;
    TextView loginText;
    Button signupButton;
    DatabaseHelper databaseHelper;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username = findViewById(R.id.userNameText);
        password = findViewById(R.id.userPasswordText);
        confirmPassword = findViewById(R.id.userConfirmPasswordText);
        loginText = findViewById(R.id.textSignup);
        signupButton = findViewById(R.id.signButton);
        databaseHelper = new DatabaseHelper(this);

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v-> signupValidation());
    }
    // Toast message
    private void toastText (String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    private void signupValidation() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        // Basic input validation
        if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            toastText("Please fill all fields");
        }else if(!pass.equals(confirmPass)){
            toastText("Passwords do not match");
        } else {
            // Check if username already exists
            if(databaseHelper.isUsernameExists(user)){
                //Username already exists, show an error
                toastText("Username already exists. Please choose a different username");
            } else{
                // Username is unique, proceed with registration
                if(databaseHelper.insertData(user,pass)){
                    toastText("Registration Successful. Please log in");
                    Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                    intent.putExtra("USERNAME",user);
                    startActivity(intent);
                    finish();
                }else{
                    toastText("Registration failed");
                }
            }
        }
    }
}