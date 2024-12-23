package com.main.tfm.support.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "userData.db";
    public static final String USER_CONTENT_TABLE = "userContentList";
    public static final String USER_GOAL_TABLE = "userGoals";
    public static final String USER_TAGS_TABLE = "userContentTags";

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
        sqLiteDatabase.execSQL("CREATE TABLE " + USER_TAGS_TABLE + " (" +
                "    id TEXT PRIMARY KEY," +
                "    name TEXT NOT NULL," +
                "    type TEXT NOT NULL," +
                "    userScore INTEGER," +
                "    poster TEXT ," +
                "    genres TEXT," +
                "    tags TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + USER_CONTENT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE " + USER_GOAL_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE " + USER_TAGS_TABLE);
        onCreate(sqLiteDatabase);
    }
}
