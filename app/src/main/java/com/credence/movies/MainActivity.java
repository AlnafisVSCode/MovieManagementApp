package com.credence.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnRegister).setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        findViewById(R.id.btnDisplay).setOnClickListener(view -> {
            startActivity(new Intent(this, DisplayActivity.class));
        });

        findViewById(R.id.btnFavourite).setOnClickListener(view -> {
            startActivity(new Intent(this, DisplayActivity.class).putExtra("fav", true));
        });

        findViewById(R.id.btnEdit).setOnClickListener(view -> {
            startActivity(new Intent(this, DisplayActivity.class).putExtra("edit_mode", true));
        });
        findViewById(R.id.btnSearch).setOnClickListener(view -> {
            startActivity(new Intent(this, DisplayActivity.class).putExtra("search_mode", true));
        });
        findViewById(R.id.btnRatings).setOnClickListener(view -> {
            startActivity(new Intent(this, DisplayActivity.class).putExtra("imdb_mode", true));
        });

        final List<Movie> w = new DBManager(this).getListOfMovies("w");
        for (Movie h : w) {
            Log.d(TAG, "onCreate: " + h);
        }
    }
}