package com.credence.movies;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.midi.MidiOutputPort;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.credence.movies.DatabaseHelper.CAST;
import static com.credence.movies.DatabaseHelper.DIRECTOR;
import static com.credence.movies.DatabaseHelper.FAVOURITE;
import static com.credence.movies.DatabaseHelper.MOVIE_NAME;
import static com.credence.movies.DatabaseHelper.RATING;
import static com.credence.movies.DatabaseHelper.REVIEW;
import static com.credence.movies.DatabaseHelper.TABLE_NAME;
import static com.credence.movies.DatabaseHelper.YEAR;
import static com.credence.movies.DatabaseHelper._ID;

public class DBManager {
    private static final String TAG = "DBManager";
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * TO store data
     */
    public void insert(Movie movie) {
        open();
        database.insert(DatabaseHelper.TABLE_NAME, null, movie.getContentValues());
    }

    /**
     * @return to get data from database
     */
    public List<Movie> getFavouriteMovie() {
        open();
        Cursor cursor = database.rawQuery(String.format("select * from %s where %s = %s", TABLE_NAME, FAVOURITE, 1), null);
        List<Movie> movieList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            movieList.add(new Movie()
                    .setId(cursor.getInt(cursor.getColumnIndex(_ID)))
                    .setName(cursor.getString(cursor.getColumnIndex(MOVIE_NAME)))
                    .setCast(cursor.getString(cursor.getColumnIndex(CAST)))
                    .setDirector(cursor.getString(cursor.getColumnIndex(DIRECTOR)))
                    .setRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RATING))))
                    .setReview(cursor.getString(cursor.getColumnIndex(REVIEW)))
                    .setYear(Integer.parseInt(cursor.getString(cursor.getColumnIndex(YEAR))))
                    .setFavourite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FAVOURITE)))));
            cursor.moveToNext();
        }
        return movieList;
    }

    public Movie getMovie(int index) {
        open();
        Cursor cursor = database.rawQuery(String.format("select * from %s where %s = %s", TABLE_NAME, _ID, index), null);
        List<Movie> movieList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            movieList.add(new Movie()
                    .setId(cursor.getInt(cursor.getColumnIndex(_ID)))
                    .setName(cursor.getString(cursor.getColumnIndex(MOVIE_NAME)))
                    .setCast(cursor.getString(cursor.getColumnIndex(CAST)))
                    .setDirector(cursor.getString(cursor.getColumnIndex(DIRECTOR)))
                    .setRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RATING))))
                    .setReview(cursor.getString(cursor.getColumnIndex(REVIEW)))
                    .setYear(Integer.parseInt(cursor.getString(cursor.getColumnIndex(YEAR))))
                    .setFavourite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FAVOURITE)))));
            cursor.moveToNext();
            break;
        }
        return movieList.get(0);
    }

    public List<Movie> getAllMovies() {
        open();
        Cursor cursor = database.rawQuery(String.format("select * from %s", TABLE_NAME), null);
        List<Movie> movieList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            movieList.add(new Movie()
                    .setId(cursor.getInt(cursor.getColumnIndex(_ID)))
                    .setName(cursor.getString(cursor.getColumnIndex(MOVIE_NAME)))
                    .setCast(cursor.getString(cursor.getColumnIndex(CAST)))
                    .setDirector(cursor.getString(cursor.getColumnIndex(DIRECTOR)))
                    .setRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RATING))))
                    .setReview(cursor.getString(cursor.getColumnIndex(REVIEW)))
                    .setYear(Integer.parseInt(cursor.getString(cursor.getColumnIndex(YEAR))))
                    .setFavourite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FAVOURITE)))));
            cursor.moveToNext();
        }
        return movieList;
    }

    /**
     * To update values in database
     */
    public int update(long _id, Movie movie) {
        Log.d(TAG, "update() called with: _id = [" + _id + "], movie = [" + movie.toString() + "]");
        open();
        return database.update(DatabaseHelper.TABLE_NAME, movie.getContentValues(), DatabaseHelper._ID + " = " + _id, null);
    }

    public List<Movie> getListOfMovies(String searchText) {
        open();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s LIKE '%%%s%%' or %s LIKE '%%%s%%' or %s LIKE '%%%s%%';",
                TABLE_NAME, MOVIE_NAME, searchText, DIRECTOR, searchText, CAST, searchText), null);
        List<Movie> movieList = new ArrayList<>();
        Log.d(TAG, "getListOfMovies: "+cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            movieList.add(new Movie()
                    .setId(cursor.getInt(cursor.getColumnIndex(_ID)))
                    .setName(cursor.getString(cursor.getColumnIndex(MOVIE_NAME)))
                    .setCast(cursor.getString(cursor.getColumnIndex(CAST)))
                    .setDirector(cursor.getString(cursor.getColumnIndex(DIRECTOR)))
                    .setRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RATING))))
                    .setReview(cursor.getString(cursor.getColumnIndex(REVIEW)))
                    .setYear(Integer.parseInt(cursor.getString(cursor.getColumnIndex(YEAR))))
                    .setFavourite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FAVOURITE)))));
            cursor.moveToNext();
            break;
        }
        return movieList;
    }
}