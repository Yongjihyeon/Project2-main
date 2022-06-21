package com.android.project2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CalendarDBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";

    public CalendarDBHelper(Context context) {
        super(context, CalendarUserContract.DB_NAME, null, CalendarUserContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(CalendarUserContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(CalendarUserContract.Users.DELETE_TABLE);
        onCreate(db);
    }

    public void insertUserBySQL(String date, String title, String startHour, String endHour, String place, String memo) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s')",
                    CalendarUserContract.Users.TABLE_NAME,
                    CalendarUserContract.Users._ID,
                    CalendarUserContract.Users.KEY_DATE,
                    CalendarUserContract.Users.KEY_TITLE,
                    CalendarUserContract.Users.KEY_STARTTIME,
                    CalendarUserContract.Users.KEY_ENDTIME,
                    CalendarUserContract.Users.KEY_PLACE,
                    CalendarUserContract.Users.KEY_MEMO,
                    date, title, startHour, endHour, place, memo);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in inserting recodes");
        }
    }

    public Cursor getTitleBySQL() {
        String sql = "Select * FROM " + CalendarUserContract.Users.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public void deleteUserBySQL(String date) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    CalendarUserContract.Users.TABLE_NAME,
                    CalendarUserContract.Users.KEY_DATE,
                    date);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }


}
