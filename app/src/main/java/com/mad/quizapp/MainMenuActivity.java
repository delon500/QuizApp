package com.mad.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainMenuActivity extends AppCompatActivity {
    Button startQuizButton, viewPastScoresButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        startQuizButton = findViewById(R.id.startQuizButton);
        viewPastScoresButton = findViewById(R.id.viewPastScoresButton);
        logoutButton = findViewById(R.id.logoutButton);

        startQuizButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, QuizActivity.class);
            startActivity(intent);
        });

        viewPastScoresButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, PastScoresActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            // Clear user session and navigate back to login screen
            SharedPreferences sharedPreferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("USER_ID");
            editor.apply();

            Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}