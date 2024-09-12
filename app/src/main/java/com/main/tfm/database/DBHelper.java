package com.main.tfm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userData.db";
    public static final String USER_CONTENT_TABLE = "userContentList";
    public static final String USER_GOAL_TABLE = "userGoals";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + USER_CONTENT_TABLE + " (" +
                "    id TEXT PRIMARY KEY," +
                "    name TEXT NOT NULL," +
                "    poster TEXT," +
                "    type TEXT," +
                "    userScore INTEGER," +
                "    userReview TEXT," +
                "    status TEXT" +
                ");");
        sqLiteDatabase.execSQL("CREATE TABLE " + USER_GOAL_TABLE + " (" +
                "    year TEXT PRIMARY KEY," +
                "    moviesTarget INTEGER," +
                "    showsTarget INTEGER," +
                "    videogamesTarget INTEGER," +
                "    booksTarget INTEGER," +
                "    moviesCompleted INTEGER," +
                "    showsCompleted INTEGER," +
                "    videogamesCompleted INTEGER," +
                "    booksCompleted INTEGER" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + USER_CONTENT_TABLE);
        onCreate(sqLiteDatabase);
    }
}
