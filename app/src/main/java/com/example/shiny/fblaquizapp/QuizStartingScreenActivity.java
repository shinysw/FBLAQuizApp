package com.example.shiny.fblaquizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class QuizStartingScreenActivity extends AppCompatActivity {

    //Constant variable initializations
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";

    //Variable initialization for the various widgets
    private Spinner spinnerCategory;
    private TextView textViewHighscore;
    private TextView textViewInfoScreen;

    //Variable initialization for displaying the high schore
    private int highscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets layout for the starting quiz screen
        setContentView(R.layout.activity_quiz_starting_screen);

        //Initializes the button widget
        Button buttonStartQuiz = findViewById(R.id.startQuizButton);
        textViewHighscore = findViewById(R.id.textViewHighscore);
        textViewInfoScreen = findViewById(R.id.textViewInfoActivity);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        //Obtains the categories from the database, and uses Array Adapter in order to convert them into a format that the spinner widget can use
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();
        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapterCategories);


        //Calls method to load the high score
        loadHighScore();

        //Listener and intent creator for starting the quiz using the button
        textViewInfoScreen.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent infoIntent = new Intent(QuizStartingScreenActivity.this, InfoActivity.class);
                                                      QuizStartingScreenActivity.this.startActivity(infoIntent);
                                                  }
                                              }

        );

        //Listener and intent creator for going to the info screen through the text
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();

            }
        });
    }

    //Private method for creating an intent that opens the java file.
    private void startQuiz() {
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();

        Intent intent = new Intent(QuizStartingScreenActivity.this, QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    //Obtains the data from the Quiz Activity page, including the highscore
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
    }

    //Loads the stored high score
    private void loadHighScore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore: " + highscore);
    }

    //Updates the highscore if it is higher
    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();

    }
}
