package com.main.tfm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import com.main.tfm.APIAccess.Content;
import com.main.tfm.APIAccess.UserContent;

public class UserDB extends DBHelper{

    Context context;

    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

    public UserDB(@Nullable Context context) {
        super(context);
        this.context=context;
    }

    public long addContent(String id, String name, String poster, String type, int userScore, String userReview, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long result;
        values.put("id", id);
        values.put("name", name);
        values.put("poster", poster);
        values.put("type", type);
        values.put("userScore", userScore);
        values.put("userReview", userReview);
        values.put("status", status);
        result = db.insert(USER_CONTENT_TABLE, null, values);
        db.close();
        return result;
    }

    public long addContent(UserContent item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long result;
        values.put("id", item.getId());
        values.put("name", item.getTitle());
        values.put("poster", item.getPoster());
        values.put("type", item.getType());
        values.put("userScore", item.getScore());
        values.put("userReview", item.getReview());
        values.put("status", item.getStatus());
        result = db.insert(USER_CONTENT_TABLE, null, values);
        db.close();
        return result;
    }

    public boolean editContent(UserContent item){
        boolean updateOK = false;
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE " + USER_CONTENT_TABLE + " SET userScore = " + item.getScore() + ", userReview = '" + item.getReview() + "', status = '" + item.getStatus() + "' WHERE id = '" + item.getId() + "' ");
            updateOK=true;
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }finally {
            db.close();
        }
        return updateOK;
    }

    public ArrayList<Content> retrieveContentList(){
        ArrayList<Content> contentList = new ArrayList<>();
        String id, name, poster, type, userReview, status;
        int userScore;
        UserContent item;
        try{
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor contentCursor;

            contentCursor = db.rawQuery("SELECT * FROM " + USER_CONTENT_TABLE, null);

            if(contentCursor.moveToFirst()){
                do{
                    id = contentCursor.getString(0);
                    name = contentCursor.getString(1);
                    poster = contentCursor.getString(2);
                    type = contentCursor.getString(3);
                    userScore = contentCursor.getInt(4);
                    userReview = contentCursor.getString(5);
                    status = contentCursor.getString(6);
                    item = new UserContent(id, name, "", poster, type, userScore, userReview, status);
                    contentList.add(item);
                }while(contentCursor.moveToNext());
            }
            contentCursor.close();
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }
        return contentList;
    }

    public ArrayList<UserContent> retrieveContentByStatus(String status){
        ArrayList<UserContent> contentList = new ArrayList<>();
        String id, name, poster, type, userReview;
        int userScore;
        UserContent item;
        try{
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor contentCursor;

            contentCursor = db.rawQuery("SELECT * FROM " + USER_CONTENT_TABLE + " WHERE status = '" + status + "'", null);

            if(contentCursor.moveToFirst()){
                do{
                    id = contentCursor.getString(0);
                    name = contentCursor.getString(1);
                    poster = contentCursor.getString(2);
                    type = contentCursor.getString(3);
                    userScore = contentCursor.getInt(4);
                    userReview = contentCursor.getString(5);
                    item = new UserContent(id, name, "", poster, type, userScore, userReview, status);
                    contentList.add(item);
                }while(contentCursor.moveToNext());
            }
            contentCursor.close();
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }
        return contentList;
    }

    public UserContent checkContent(String id){
        UserContent result = new UserContent();
        String name, poster, type, userReview, status;
        int userScore;
        try{
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor contentCursor;
            contentCursor = db.rawQuery("SELECT * FROM " + USER_CONTENT_TABLE + " WHERE id = '" + id + "' LIMIT 1", null);
            if(contentCursor.moveToFirst()){
                name = contentCursor.getString(1);
                poster = contentCursor.getString(2);
                type = contentCursor.getString(3);
                userScore = contentCursor.getInt(4);
                userReview = contentCursor.getString(5);
                status = contentCursor.getString(6);
                result = new UserContent(id, name, "", poster, type, userScore, userReview, status);
            }else return null;
            contentCursor.close();
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }
        return result;
    }

    public boolean deleteContentItem(String id){
        boolean deleteOK = false;
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + USER_CONTENT_TABLE + " WHERE id = '" + id + "'");
            deleteOK=true;
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }finally {
            db.close();
        }
        return deleteOK;
    }

    public ArrayList<Integer> retrieveNumberOfItemsByType(){
        ArrayList<Integer> result = new ArrayList<>();
        int nMovies, nShows, nGames, nBooks;
        nMovies = nShows = nGames = nBooks = 0;
        try{
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor contentCursor;

            contentCursor = db.rawQuery("SELECT * FROM " + USER_CONTENT_TABLE + ";", null);

            if(contentCursor.moveToFirst()){
                do{
                    switch (contentCursor.getString(3)){
                        case "movie":
                           nMovies++;
                            break;
                        case "tvshow":
                            nShows++;
                            break;
                        case "videogame":
                            nGames++;
                            break;
                        case "book":
                            nBooks++;
                            break;
                    }

                }while(contentCursor.moveToNext());
            }
            result.add(nMovies);
            result.add(nShows);
            result.add(nGames);
            result.add(nBooks);
            contentCursor.close();
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }
        return result;
    }

    public long addUserGoals(int movies, int shows, int videogames, int books){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long result;
        values.put("year", currentDate);
        values.put("moviesTarget", movies);
        values.put("showsTarget", shows);
        values.put("videogamesTarget", videogames);
        values.put("moviesCompleted", books);
        values.put("showsCompleted", 0);
        values.put("videogamesCompleted", 0);
        values.put("booksCompleted", 0);
        result = db.insert(USER_GOAL_TABLE, null, values);
        db.close();
        return result;
    }

    public boolean updateGoalTable(String type, int updatedGoal){
        boolean updateOK = false;
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            switch (type){
                case "movie":
                    db.execSQL("UPDATE " + USER_GOAL_TABLE + " SET moviesCompleted = " + updatedGoal +" WHERE year = '" + currentDate + "' ;");
                    break;
                case "show":
                    db.execSQL("UPDATE " + USER_GOAL_TABLE + " SET showsCompleted = " + updatedGoal +" WHERE year = '" + currentDate + "' ;");
                    break;
                case "videogame":
                    db.execSQL("UPDATE " + USER_GOAL_TABLE + " SET videogamesCompleted = " + updatedGoal +" WHERE year = '" + currentDate + "' ;");
                    break;
                case "book":
                    db.execSQL("UPDATE " + USER_GOAL_TABLE + " SET booksCompleted = " + updatedGoal +" WHERE year = '" + currentDate + "' ;");
                    break;
            }
            updateOK=true;
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }finally {
            db.close();
        }
        return updateOK;
    }

    public boolean editGoalTable(String type, int updatedTarget){
        boolean updateOK = false;
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            switch (type){
                case "movie":
                    db.execSQL("UPDATE " + USER_GOAL_TABLE + " SET moviesTarget = " + updatedTarget +" WHERE year = '" + currentDate + "' ;");
                    break;
                case "show":
                    db.execSQL("UPDATE " + USER_GOAL_TABLE + " SET showsTarget = " + updatedTarget +" WHERE year = '" + currentDate + "' ;");
                    break;
                case "videogame":
                    db.execSQL("UPDATE " + USER_GOAL_TABLE + " SET videogamesTarget = " + updatedTarget +" WHERE year = '" + currentDate + "' ;");
                    break;
                case "book":
                    db.execSQL("UPDATE " + USER_GOAL_TABLE + " SET booksTarget = " + updatedTarget +" WHERE year = '" + currentDate + "' ;");
                    break;
            }
            updateOK=true;
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }finally {
            db.close();
        }
        return updateOK;
    }

    public int retrieveGoalCompletionByType(String type){
        int result = -1;
        try{
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor contentCursor;

            contentCursor = db.rawQuery("SELECT "+ type +"Completed FROM " + USER_GOAL_TABLE + ";", null);

            if(contentCursor.moveToFirst()){
                result = contentCursor.getInt(0);
            }
            contentCursor.close();
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }
        return result;
    }

    public void purgeDatabase(){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE " + USER_CONTENT_TABLE);
        db.execSQL("DROP TABLE " + USER_GOAL_TABLE);
        db.execSQL("CREATE TABLE " + USER_CONTENT_TABLE + " (" +
                "    id TEXT PRIMARY KEY," +
                "    name TEXT NOT NULL," +
                "    poster TEXT," +
                "    type TEXT," +
                "    userScore INTEGER," +
                "    userReview TEXT," +
                "    status TEXT" +
                ");");
        db.execSQL("CREATE TABLE " + USER_GOAL_TABLE + " (" +
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

}
