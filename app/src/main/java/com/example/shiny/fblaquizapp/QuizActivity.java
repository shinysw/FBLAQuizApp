package com.example.shiny.fblaquizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    //Constant variables, which is why they are all in caps and finals
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "KeyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    //Names for all of the text, widgets such as button, answer choices etc
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionNumber;
    private TextView textViewCountDown;
    private TextView textViewCategory;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirm;

    //Variables for colors
    private ColorStateList textColorDefault;
    private ColorStateList textColorDefaultCd;

    //Variables for the timer
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private long backPressedTime;

    //Variable initialization for the array list of questions
    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;

    //Variables for the question objects and various other important variables
    private Question currentQuestion;
    private int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Assigns names to the textviews, widgets, answer choices, etc.
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestionNumber = findViewById(R.id.textViewQuestionNumber);
        textViewCountDown = findViewById(R.id.textViewTimer);
        textViewCategory = findViewById(R.id.textViewCategory);
        rbGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);
        buttonConfirm = findViewById(R.id.buttonConfirm);

        //Variable for default text color
        textColorDefault = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();

        //Creates an intent to get the category ID's
        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(QuizStartingScreenActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(QuizStartingScreenActivity.EXTRA_CATEGORY_NAME);

        //Sets the value of the text view to the correct category name
        textViewCategory.setText("Category:\n" + categoryName);

        //DB helper for SQLite integration
        if (savedInstanceState == null) {

            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID);

            //Gets the Question Count Total
            questionCountTotal = questionList.size();

            //Shuffles the questions
            Collections.shuffle(questionList);

            //Shows the next question
            showNextQuestion();
        } else
        {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            //Checks if the question has been answered yet, and acts accordingly
            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }

        }

        //Event listener for the button
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If else to make sure the question is answered. If it isn't, a small pop up appears
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                    }
                    else
                    {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    showNextQuestion();
                }
            }
        });
    }

    //Private method for showing the next question
    private void showNextQuestion() {
        //Reverts all of the colors to the default color
        rb1.setTextColor(textColorDefault);
        rb2.setTextColor(textColorDefault);
        rb3.setTextColor(textColorDefault);
        rb4.setTextColor(textColorDefault);

        //Unchecks all of the answer choices
        rbGroup.clearCheck();


        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            textViewQuestionNumber.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirm.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }
        else
        {
            finishQuiz();
        }


    }

    //Private method for starting the count down
    //Uses the countDownTimer library
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                //Calls the appropriate method to update the text.
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                //Timer will display 1 seconds even though there is supposed to be 0, so this fixes that
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);

        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    //Private method for checking if the answer is correct or incorrect
    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        //We add one because we decided not to start at the usual 0
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            textViewScore.setText("Score: " + score);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        //Switch case question for changing the correct color of the answer
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor((Color.GREEN));
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor((Color.GREEN));
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor((Color.GREEN));
                textViewQuestion.setText("Answer 3 is correct");
                break;
            case 4:
                rb4.setTextColor((Color.GREEN));
                textViewQuestion.setText("Answer 4 is correct");
                break;
        }

        if (questionCounter < questionCountTotal) {
            buttonConfirm.setText("Next");
        }
        else
            {
                buttonConfirm.setText("Confirm");
            }
        }
    private void finishQuiz()
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    //Prevents the user from accidentally going back and leaving the quiz screen
    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        }else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }

}

