package com.credence.movies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "MOVIES";

    // Table columns
    public static final String _ID = "_id";
    public static final String MOVIE_NAME = "name";
    public static final String YEAR = "year";
    public static final String DIRECTOR = "director";
    public static final String CAST = "_cast";
    public static final String RATING = "rating";
    public static final String REVIEW = "review";
    public static final String FAVOURITE = "favourite";

    // Database Information
    static final String DB_NAME = "DB_MOVIES.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MOVIE_NAME + " TEXT NOT NULL, " +
            DIRECTOR + " TEXT NOT NULL, " +
            CAST + " TEXT NOT NULL, " +
            REVIEW + " TEXT NOT NULL, " +
            RATING + " INTEGER NOT NULL, " +
            FAVOURITE + " INTEGER NOT NULL, " +
            YEAR + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}