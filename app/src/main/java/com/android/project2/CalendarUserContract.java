package com.android.project2;

import android.provider.BaseColumns;

public class CalendarUserContract {
    public static final String DB_NAME="Calendar.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CalendarUserContract() {}

    /* Inner class that defines the table contents */
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME="Users";
        public static final String KEY_NAME = "Name";
        public static final String KEY_TITLE = "Title";
        public static final String KEY_STARTHOUR = "StartTime";
        public static final String KEY_ENDHOUR = "EndTime";
        public static final String KEY_PLACE = "Place";
        public static final String KEY_MEMO = "Memo";


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_NAME + TEXT_TYPE + COMMA_SEP +
                KEY_TITLE + TEXT_TYPE + COMMA_SEP +
                KEY_STARTHOUR + TEXT_TYPE + COMMA_SEP +
                KEY_ENDHOUR + TEXT_TYPE + COMMA_SEP +
                KEY_PLACE + TEXT_TYPE + COMMA_SEP +
                KEY_MEMO + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
