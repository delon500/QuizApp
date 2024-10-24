package com.mad.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PastScoresActivity extends AppCompatActivity {
    RecyclerView scoreRecyclerView;
    ScoresAdapter scoresAdapter;
    List<UserScore> userScores;
    DatabaseHelper databaseHelper;
    int userId;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_scores);

        scoreRecyclerView = findViewById(R.id.scoresRecyclerView);
        backButton = findViewById(R.id.backButton);

        // Set up RecyclerView
        scoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("QuizAppPrefs",MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID",-1);

        // Load user scores from the database
        userScores = databaseHelper.getUserScores(userId);

        scoresAdapter = new ScoresAdapter(userScores);
        scoreRecyclerView.setAdapter(scoresAdapter);

        backButton.setOnClickListener(v ->{
            finish();
        });

    }
}