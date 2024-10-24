package com.mad.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreSummaryActivity extends AppCompatActivity {
    TextView scoreText, percentageText;
    Button viewPastScoresButton, retakeQuizButton, mainMenuButton;
    int correctAnswers, totalQuestions;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainMenuIntent = new Intent(ScoreSummaryActivity.this, QuizActivity.class);
        startActivity(mainMenuIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_summary);

        scoreText = findViewById(R.id.scoreTextView);
        percentageText = findViewById(R.id.percentageTextView);
        viewPastScoresButton = findViewById(R.id.viewPastScoresButton);
        retakeQuizButton = findViewById(R.id.retakeQuizButton);
        mainMenuButton= findViewById(R.id.mainMenuButton);

        Intent intent = getIntent(); // Retrieve score data from intent
        correctAnswers = intent.getIntExtra("CORRECT_ANSWERS",0);
        totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS",0);

        // Display the score
        scoreText.setText("Your Score: "+ correctAnswers +" out of "+ totalQuestions);

        int percentage = (int) ((double) correctAnswers/totalQuestions * 100);
        percentageText.setText("Percentage: "+percentage+"%");

        viewPastScoresButton.setOnClickListener(v->{
            Intent pastScoresIntent = new Intent(ScoreSummaryActivity.this, PastScoresActivity.class);
            startActivity(pastScoresIntent);
        });

        retakeQuizButton.setOnClickListener(v ->{
            Intent retakeQuizIntent = new Intent(ScoreSummaryActivity.this, QuizActivity.class);
            startActivity(retakeQuizIntent);
            finish();
        });

        mainMenuButton.setOnClickListener(v -> {
            Intent mainMenuIntent = new Intent(ScoreSummaryActivity.this, MainMenuActivity.class);
            startActivity(mainMenuIntent);
            finish();
        });

    }
}