package com.example.shiny.fblaquizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.shiny.fblaquizapp.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FBLAQuizQuestions.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private  SQLiteDatabase db;

    private QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    //Makes it so that only one database connection is established at once
    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + " ( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " + ")";


        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

        fillCategoriesTable();
        fillQuestionsTable();
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        super.onConfigure(sqLiteDatabase);
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Competitive Events");
        addCategory(c1);
        Category c2 = new Category("Business Skills");
        addCategory(c2);
        Category c3 = new Category("National Officers");
        addCategory(c3);
        Category c4 = new Category("Parliamentary Procedure");
        addCategory(c4);
        Category c5 = new Category("FBLA History");
        addCategory(c5);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("Which type of test can competitors NOT compete in?",
                "A. Objective Tests",
                "B. Objective Test & Role Play",
                "C. Production & Objective Test",
                "D. Presentation & Objective Test",
                4,
                Category.Competitive_Events);
        addQuestion(q1);

        Question q2 = new Question("How many winners are recognized during the awards ceremony for pilot events?",
                "A. Top 3",
                "B. Top 4",
                "C. Top 5",
                "D. Top 6",
                3,
                Category.Competitive_Events);
        addQuestion(q2);

        Question q3 = new Question("Which type of events are best for 9-10th graders?",
                "A. Starter to _____",
                "B. Introduction to _____",
                "C. Initiation to _____",
                "D. Level 1: _____ ",
                2,
                Category.Competitive_Events);
        addQuestion(q3);

        Question q4 = new Question("Which of the following is the only event that requires exactly 2 people?",
                "A. Life Smarts Virtual Business Management Challenge",
                "B. Help Desk",
                "C. Sports & Entertainment Management",
                "D. Sales Presentation",
                1,
                Category.Competitive_Events);
        addQuestion(q4);

        Question q5 = new Question("How many winners are recognized during the awards ceremony for open events?",
                "A. Top 1",
                "B. Top 2",
                "C. Top 3",
                "D. Top 4",
                1,
                Category.Competitive_Events);
        addQuestion(q5);

        Question q6 = new Question("When a letter or memorandum extends beyond one page, the second and succeeding page heading should consist of:",
                "A. the name of the sender and the date",
                "B. the name of the addressee and page number",
                "C. the name of the addressee, the page number, and the e-mail address",
                "D. the name of the addressee, the page number, and the date",
                4,
                Category.Business_Skills);
        addQuestion(q6);

        Question q7 = new Question("In a large company, to whom would an operational manager generally report?",
                "A. A head manager",
                "B. A board manager",
                "C. A middle manager",
                "D. An executive manager",
                3,
                Category.Business_Skills);
        addQuestion(q7);

        Question q8 = new Question("When a letter or memorandum extends beyond one page, the second and succeeding page heading should consist of:",
                "A. the name of the sender and the date",
                "B. the name of the addressee and page number",
                "C. the name of the addressee, the page number, and the e-mail address",
                "D. the name of the addressee, the page number, and the date",
                4,
                Category.Business_Skills);
        addQuestion(q8);

        Question q9 = new Question("What is OSHA?",
                "A. Occupational Safety and Health Administration",
                "B. Operational Safety and Hiring Administration",
                "C. Onboard Safe and High Administration",
                "D. Operating Safety and Height Administration",
                1,
                Category.Business_Skills);
        addQuestion(q9);

        Question q10 = new Question("Which firms sell stocks and bonds, but not loans?",
                "A. Stock firms",
                "B. Monetary firms",
                "C. Brokerage firms",
                "D. Savings firms",
                3,
                Category.Business_Skills);
        addQuestion(q10);

        Question q11 = new Question("Who is the National 2018-2019 FBLA President?",
                "A. Eu Ro Wang",
                "B. Lu Chang",
                "C. Ei Wong Chi",
                "D. Rei Chu",
                1,
                Category.National_Officers);
        addQuestion(q11);

        Question q12 = new Question("Who is the National 2018-2019 FBLA Secretary?",
                "A. Preethi Ranj",
                "B. Kanathi Gupta",
                "C. Keerti Soundappan",
                "D. Moghal Veera",
                3,
                Category.National_Officers);
        addQuestion(q12);

        Question q13 = new Question("Who is the National 2018-2019 FBLA Treasurer?",
                "A. Cantiss Ford",
                "B. Eliza Cantillon",
                "C. Lisa Curry",
                "D. Galadriel Coury",
                4,
                Category.National_Officers);
        addQuestion(q13);

        Question q14 = new Question("Who is the National 2018-2019 FBLA Parliamentarian?",
                "A. Kevin Lei",
                "B. Michael Zhao",
                "C. Mark Zhu",
                "D. Lily Lee",
                2,
                Category.National_Officers);
        addQuestion(q14);

        Question q15 = new Question("When is the application for the 2019-2020 National Term?",
                "A. 11:59 p.m. Eastern Time on May 12",
                "B. 11:59 p.m. Eastern Time on May 13",
                "C. 11:59 p.m. Eastern Time on May 14",
                "D. 11:59 p.m. Eastern Time on May 15",
                4,
                Category.Parliamentary_Procedure);
        addQuestion(q15);

        Question q16 = new Question("What is the motion to close the meeting?",
                "A. End meeting",
                "B. Conclude meeting",
                "C. Adjourn meeting",
                "D. Dismiss meeting",
                3,
                Category.Parliamentary_Procedure);
        addQuestion(q16);

        Question q17 = new Question("Who is the person that presides over meetings?",
                "A. The chair",
                "B. The lead",
                "C. The head",
                "D. The presding eveninge",
                1,
                Category.Parliamentary_Procedure);
        addQuestion(q17);

        Question q18 = new Question("What is the motion to modify the wording of a motion?",
                "A. A modification",
                "B. A change",
                "C. A movement",
                "D. A amendment",
                4,
                Category.Parliamentary_Procedure);
        addQuestion(q18);

        Question q19 = new Question("Who prepares and reads the minutes of the meeting?",
                "A. The head speaker",
                "B. The orator",
                "C. The secretary",
                "D. The organizer",
                3,
                Category.Parliamentary_Procedure);
        addQuestion(q19);

        Question q20 = new Question("Which motion introduces bringing business before the assembly?",
                "A. Order",
                "B. Business",
                "C. Main",
                "D. Introduction",
                3,
                Category.Parliamentary_Procedure);
        addQuestion(q20);

        Question q21 = new Question("According to the FBLA creed, education is the rate of:",
                "A. Every student",
                "B. Every person",
                "C. Every human",
                "D. Everybody",
                1,
                Category.FBLA_History);
        addQuestion(q21);

        Question q22 = new Question("The award in parliamentary procedure event is named for whom?",
                "A. Abraham Lincoln",
                "B. Edward D Miller",
                "C. Eugene Mills",
                "D. John Rawls",
                2,
                Category.FBLA_History);
        addQuestion(q22);

        Question q23 = new Question("When was the grand opening of the FBLA-PBL National Center?",
                "A. 1988",
                "B. 1989",
                "C. 1990",
                "D. 1991",
                4,
                Category.FBLA_History);
        addQuestion(q23);

        Question q24 = new Question("Who is the Father of FBLA?",
                "A. Hamden L. Forkner",
                "B. Christopher Campbell",
                "C. Kurt Phillips",
                "D. Robert Derald",
                1,
                Category.FBLA_History);
        addQuestion(q24);

        Question q25 = new Question("Where was the first National Leadership Conference held?",
                "A. Los Angeles, California",
                "B. Ithaca, New York",
                "C. Chicago, Illinois",
                "D. Baltimore, Maryland",
                3,
                Category.FBLA_History);
        addQuestion(q25);


    }

    private void addCategory(Category category){
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        //We do not need null cells so nullColumn is null
        db.insert(QuestionsTable.TABLE_NAME, null,cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToNext()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            }while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }



    public ArrayList<Question> getAllQuestions () {
        //Variable initialization for the arraylist
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                //Creates question object
                Question question = new Question();

                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));

                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

        public ArrayList<Question> getQuestions(int categoryID) {
            ArrayList<Question> questionList = new ArrayList<>();
            db = getReadableDatabase();

            String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? ";

            String[] selectionArgs = new String[]{String.valueOf(categoryID)};

            Cursor c = db.query(
                    QuestionsTable.TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (c.moveToFirst()) {
                do {
                    Question question = new Question();
                    question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                    question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                    question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                    question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                    question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                    question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                    question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                    question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                    questionList.add(question);
                } while (c.moveToNext());
            }

        c.close();
        return questionList;
    }
}
