package com.main.tfm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import APIAccess.Content;
import APIAccess.Movies_TVShows.Movie;
import APIAccess.UserContent;

public class UserDB extends DBHelper{

    Context context;

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
        result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
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

            contentCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

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

            contentCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE status = " + status, null);

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

    public boolean deleteContentItem(String id){
        boolean deleteOK = false;
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_NAME + "WHERE id = '" + id + "'");
            deleteOK=true;
        }catch (Exception ex){
            Log.i("BD", ex.toString());
        }finally {
            db.close();
        }
        return deleteOK;
    }

}
