package com.mad.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QUIZ.db";
    private static final String USER_TABLE = "USER";
    private static final String COLUMN_USER_ID="USER_ID";
    private static final String COLUMN_USER_NAME = "USERNAME";
    private static final String COLUMN_USER_PASSWORD = "PASSWORD";

    private static final String QUESTION_TABLE = "QUESTION";
    private static final String COLUMN_QUESTION_ID = "QUESTION_ID";
    private static final String COLUMN_QUESTION_TEXT ="QUESTION_TEXT";
    private static final String COLUMN_QUESTION_CATEGORY ="CATEGORY";
    private static final String COLUMN_QUESTION_DIFFICULTY = "DIFFICULTY";

    private static final String ANSWER_TABLE = "ANSWER";
    private static final String COLUMN_ANSWER_ID = "ANSWER_ID";
    private static final String COLUMN_ANSWER_TEXT = "ANSWER_TEXT";
    private static final String COLUMN_IS_CORRECT = "IS_CORRECT";

    private static final String USER_SCORE_TABLE = "USER_SCORES";
    private static final String COLUMN_SCORE_ID = "SCORE_ID";
    private static final String COLUMN_SCORE = "SCORE";
    private static final String COLUMN_TOTAL_QUESTION ="TOTAL_QUESTION";
    private static final String COLUMN_CORRECT_ANSWERS ="CORRECT_ANSWERS";
    private static final String COLUMN_QUIZ_DATE = "QUIZ_DATE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE "+USER_TABLE+" ("
                + COLUMN_USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_PASSWORD + " TEXT)";

        String CREATE_QUESTIONS_TABLE = "CREATE TABLE "+QUESTION_TABLE+" ("
                + COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_QUESTION_TEXT+" TEXT, "
                + COLUMN_QUESTION_CATEGORY +" TEXT, "
                + COLUMN_QUESTION_DIFFICULTY+ " TEXT)";

        String CREATE_ANSWERS_TABLE = "CREATE TABLE "+ANSWER_TABLE+" ("
                + COLUMN_ANSWER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_QUESTION_ID + " INTEGER, "
                + COLUMN_ANSWER_TEXT +" TEXT, "
                + COLUMN_IS_CORRECT +" INTEGER, "
                + "FOREIGN KEY (QUESTION_ID) REFERENCES " + QUESTION_TABLE + "(" + COLUMN_QUESTION_ID + ") ON DELETE CASCADE" + ")";

        String CREATE_USERSCORES_TABLE = "CREATE TABLE " + USER_SCORE_TABLE + " ("
                + COLUMN_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_SCORE + " INTEGER, "
                + COLUMN_TOTAL_QUESTION + " INTEGER, "
                + COLUMN_CORRECT_ANSWERS + " INTEGER, "
                + COLUMN_QUIZ_DATE + " INTEGER, "
                + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + USER_TABLE + " (" + COLUMN_USER_ID + ") ON DELETE CASCADE" + ")";


        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_QUESTIONS_TABLE);
        db.execSQL(CREATE_ANSWERS_TABLE);
        db.execSQL(CREATE_USERSCORES_TABLE);

        // Insert initial data
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_SCORE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ANSWER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public void insertInitialData(SQLiteDatabase db){
        List<Question> questions = new ArrayList<>();

        List<Answer> answers1 = new ArrayList<>();
        answers1.add(new Answer("Paris",true));
        answers1.add(new Answer("London",false));
        answers1.add(new Answer("Berlin",false));
        answers1.add(new Answer("Roma",false));
        questions.add(new Question("What is the capital of France?","Geography","Easy",answers1));

        List<Answer> answers2 = new ArrayList<>();
        answers2.add(new Answer("Oxygen", false));
        answers2.add(new Answer("Carbon", false));
        answers2.add(new Answer("Hydrogen", true));
        answers2.add(new Answer("Nitrogen", false));
        questions.add(new Question("What is the first element on the periodic table?", "Science", "Medium", answers2));

        List<Answer> answers3 = new ArrayList<>();
        answers3.add(new Answer("Liver",false));
        answers3.add(new Answer("Skin",true));
        answers3.add(new Answer("Lungs",false));
        answers3.add(new Answer("Heart",false));
        questions.add(new Question("What is the largest organ in the human body?", "Biology", "Medium", answers3));

        List<Answer> answers4 = new ArrayList<>();
        answers4.add(new Answer("Charles Dickens",false));
        answers4.add(new Answer("Jane Austen",false));
        answers4.add(new Answer("Mark Twain",false));
        answers4.add(new Answer("William Shakespeare",true));
        questions.add(new Question("Who wrote the play \"Romeo and Juliet\"?", "English", "Medium", answers4));

        // Insert questions into database
        for (Question question: questions){
            addQuestionWithAnswer(db,question);
        }

    }

    public void addQuestionWithAnswer(SQLiteDatabase db, Question question){
        ContentValues queValues = new ContentValues();
        queValues.put(COLUMN_QUESTION_TEXT,question.getQuestionText());
        queValues.put(COLUMN_QUESTION_CATEGORY,question.getCategory());
        queValues.put(COLUMN_QUESTION_DIFFICULTY,question.getDifficulty());

        long questionId = db.insert(QUESTION_TABLE,null, queValues);

        for(Answer answer: question.getAnswers()){
            ContentValues ansValues = new ContentValues();
            ansValues.put(COLUMN_QUESTION_ID,questionId);
            ansValues.put(COLUMN_ANSWER_TEXT, answer.getAnswerText());
            ansValues.put(COLUMN_IS_CORRECT,answer.isCorrect() ? 1:0);

            db.insert(ANSWER_TABLE,null, ansValues);
        }
    }

    public List<Question> getAllQuestions(){
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + QUESTION_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                int quesId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_ID));
                String quesText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_CATEGORY));
                String difficulty = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_DIFFICULTY));

                // get answers for questions
                List<Answer> answers = getAnswersForQuestion(quesId);

                Question question = new Question(quesId, quesText,category,difficulty, answers);
                questionList.add(question);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    private List<Answer> getAnswersForQuestion(int queID){
        List<Answer> answersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM "+ANSWER_TABLE+" WHERE "+COLUMN_QUESTION_ID+" =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(queID)});

        if(cursor.moveToFirst()){
            do{
                int ansId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ANSWER_ID));
                String ansText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER_TEXT));
                boolean isCorrect = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CORRECT)) == 1;

                Answer answer = new Answer(ansId,queID,ansText,isCorrect);
                answersList.add(answer);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return answersList;
    }



    public boolean insertData(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, username);

        try {
            String hashedPassword = hashPassword(password);
            cv.put(COLUMN_USER_PASSWORD,hashedPassword);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return false;
        }

        long insert = db.insert(USER_TABLE,null,cv);
        return insert!=-1;
    }

    public void insertUserScore(UserScore userScore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_ID, userScore.getUserId());
        cv.put(COLUMN_SCORE, userScore.getScore());
        cv.put(COLUMN_TOTAL_QUESTION, userScore.getTotalQuestions());
        cv.put(COLUMN_CORRECT_ANSWERS, userScore.getCorrectAnswers());
        cv.put(COLUMN_QUIZ_DATE, userScore.getQuizDate());

        db.insert(USER_SCORE_TABLE,null, cv);
        db.close();
    }

    public List<UserScore> getUserScores(int userId) {
        List<UserScore> userScores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + USER_SCORE_TABLE + " WHERE " + COLUMN_USER_ID + " = ? ORDER BY " + COLUMN_QUIZ_DATE + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                UserScore userScore = new UserScore();
                userScore.setScoreId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_ID)));
                userScore.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                userScore.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE)));
                userScore.setTotalQuestions(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUESTION)));
                userScore.setCorrectAnswers(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWERS)));
                userScore.setQuizDate(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_DATE)));

                userScores.add(userScore);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userScores;
    }


    public boolean isUsernameExists(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_NAME};
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(USER_TABLE,columns,selection,selectionArgs,null,null,null);
        boolean exists = (cursor.getCount()>0);
        cursor.close();
        return exists;
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();

        for(byte b: hash){
            String hex = Integer.toHexString(0xff & b);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public int checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_NAME + "=? AND " + COLUMN_USER_PASSWORD + "=?";
        String hashedPassword;
        try {
            hashedPassword = hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return -1;
        }
        String[] selectionArgs = {username, hashedPassword};

        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int userId = -1;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            }
            cursor.close();
        }
        return userId;
    }


    public int getUserid(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_NAME + "=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int userId = -1;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            }
            cursor.close();
        }
        return userId;
    }
}
