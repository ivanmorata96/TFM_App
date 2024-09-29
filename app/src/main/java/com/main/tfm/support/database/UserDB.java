package com.main.tfm.support.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.main.tfm.support.Content;
import com.main.tfm.support.UserContent;

public class UserDB extends DBHelper{

    Context context;

    String currentDate = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

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
                case "tvshow":
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

    public boolean editGoalTable(int movies, int shows, int videogames, int books){
        boolean updateOK = false;
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{
            db.execSQL("UPDATE " + USER_GOAL_TABLE + " SET moviesTarget = " + movies + ", " +
                    "showsTarget = " + shows + ", " +
                    "videogamesTarget = " + shows + ", " +
                    "booksTarget = " + shows +
                    " WHERE year = '" + currentDate + "' ;");
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }finally{
            db.close();
        }
        return updateOK;
    }

    public int retrieveGoalCompletionByType(String type){
        int result = -1;
        try{
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor goalsCursor;

            goalsCursor = db.rawQuery("SELECT "+ type +"Completed FROM " + USER_GOAL_TABLE + ";", null);

            if(goalsCursor.moveToFirst()){
                switch (type){
                    case "movies":
                        result = goalsCursor.getInt(5);
                        break;
                    case "shows":
                        result = goalsCursor.getInt(6);
                        break;
                    case "videogames":
                        result = goalsCursor.getInt(7);
                        break;
                    case "books":
                        result = goalsCursor.getInt(8);
                        break;
                }
            }
            goalsCursor.close();
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }
        return result;
    }

    public HashMap<String, Integer> retrieveAllGoals(){
        HashMap<String, Integer> result = new HashMap<>();
        try{
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor goalsCursor;

            goalsCursor = db.rawQuery("SELECT * FROM " + USER_GOAL_TABLE + " WHERE year=" + currentDate + ";", null);
            if(goalsCursor.moveToFirst()){
                result.put("moviesTarget", goalsCursor.getInt(1));
                result.put("showsTarget", goalsCursor.getInt(2));
                result.put("videogamesTarget", goalsCursor.getInt(3));
                result.put("booksTarget", goalsCursor.getInt(4));
                result.put("moviesCompleted", goalsCursor.getInt(5));
                result.put("showsCompleted", goalsCursor.getInt(6));
                result.put("videogamesCompleted", goalsCursor.getInt(7));
                result.put("booksCompleted", goalsCursor.getInt(8));
            }
            goalsCursor.close();
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }
        return result;
    }

    public boolean checkIfGoalsAreSet() {
        boolean ok = false;
        try {
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor goalsCursor;

            goalsCursor = db.rawQuery("SELECT * FROM " + USER_GOAL_TABLE + " WHERE year = '" + currentDate + "';", null);
            ok = goalsCursor.moveToFirst();
            goalsCursor.close();
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }
        return ok;
    }

    public void purgeDatabase(){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + USER_CONTENT_TABLE + ";");
        db.execSQL("DELETE FROM " + USER_GOAL_TABLE + ";");
    }

}
