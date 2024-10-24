package com.mad.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder> {

    private List<UserScore> userScores;

    public ScoresAdapter(List<UserScore> userScores) {
        this.userScores = userScores;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        UserScore userScore = userScores.get(position);

        // Format the date
        Date date = new Date(userScore.getQuizDate());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(holder.itemView.getContext());
        String formattedDate = dateFormat.format(date);

        // Calculate percentage
        int percentage = (int) ((double) userScore.getCorrectAnswers() / userScore.getTotalQuestions() * 100);

        // Set data to views
        holder.dateTextView.setText("Date: " + formattedDate);
        holder.scoreTextView.setText("Score: " + userScore.getCorrectAnswers() + " out of " + userScore.getTotalQuestions());
        holder.percentageTextView.setText("Percentage: " + percentage + "%");
    }

    @Override
    public int getItemCount() {
        return userScores.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, scoreTextView, percentageTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            percentageTextView = itemView.findViewById(R.id.percentageTextView);
        }
    }
}
