package com.credence.movies;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Context context;
    private EditText edtName, edtDirector, edtYear, edtCast, edtReview, edtRating;
    private boolean isEditMode = false;
    private LinearLayout linearRating;
    private int moviewIndex;
    private Movie movie;
    private ImageView imgStar1, imgStar2, imgStar3, imgStar4, imgStar5, imgStar6, imgStar7, imgStar8, imgStar9, imgStar10;
    private int rating;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey("edit_mode")) {
                    isEditMode = true;
                    if (getIntent().getExtras().containsKey("movie_index")) {
                        moviewIndex = getIntent().getExtras().getInt("movie_index");
                    }
                }
            }
        }

        initUI();

        updateData();
        setClicks();

        findViewById(R.id.btnSave).setOnClickListener(view -> {
            if (validFields()) {

                /* Code to insert movie in database*/
                try {
                    final DBManager dbManager = new DBManager(context);
                    if (isEditMode) {
                        dbManager.update(movie.getId(), movie.setName(edtName.getText().toString())
                                .setDirector(edtDirector.getText().toString())
                                .setYear(Integer.parseInt(edtYear.getText().toString()))
                                .setCast(edtCast.getText().toString())
                                .setReview(edtReview.getText().toString())
                                .setRating(rating));
                    } else {
                        dbManager.insert(new Movie()
                                .setName(edtName.getText().toString())
                                .setDirector(edtDirector.getText().toString())
                                .setYear(Integer.parseInt(edtYear.getText().toString()))
                                .setCast(edtCast.getText().toString())
                                .setReview(edtReview.getText().toString())
                                .setRating(Integer.parseInt(edtRating.getText().toString())));
                    }
                    Toast.makeText(context, "Saved!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*Click methods of UI elements*/
    private void setClicks() {
        imgStar1.setOnClickListener(view -> setReview(1));
        imgStar2.setOnClickListener(view -> setReview(2));
        imgStar3.setOnClickListener(view -> setReview(3));
        imgStar4.setOnClickListener(view -> setReview(4));
        imgStar5.setOnClickListener(view -> setReview(5));
        imgStar6.setOnClickListener(view -> setReview(6));
        imgStar7.setOnClickListener(view -> setReview(7));
        imgStar8.setOnClickListener(view -> setReview(8));
        imgStar9.setOnClickListener(view -> setReview(9));
        imgStar10.setOnClickListener(view -> setReview(10));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                movie.setFavourite(b ? 1 : 0);
            }
        });

    }

    private void updateData() {
        if (isEditMode) {
            movie = new DBManager(this).getMovie(moviewIndex);

            edtName.setText(movie.getName());
            edtYear.setText(String.valueOf(movie.getYear()));
            edtDirector.setText(movie.getDirector());
            edtReview.setText(movie.getReview());
            edtReview.setText(movie.getReview());
            edtCast.setText(movie.getCast());

            setReview(movie.getRating());
            checkBox.setChecked(movie.isFavourite());
        }
    }

    private void setReview(int index) {
        rating = index;
        Log.d(TAG, "setReview: " + rating);
        for (int i = 0; i < linearRating.getChildCount(); i++) {
            View view1 = linearRating.getChildAt(i);
            if (view1 instanceof ImageView) {
                if (i > (index - 1))
                    ((ImageView) view1).setImageResource(R.drawable.ic_baseline_star_24);
                else
                    ((ImageView) view1).setImageResource(R.drawable.ic_baseline_star_y_24);
            }
        }
    }

    /* Convert UI element to variable*/
    private void initUI() {
        edtName = findViewById(R.id.edtName);
        edtDirector = findViewById(R.id.edtDirector);
        edtYear = findViewById(R.id.edtYear);
        edtCast = findViewById(R.id.edtCast);
        edtRating = findViewById(R.id.edtRating);
        edtReview = findViewById(R.id.edtReview);
        linearRating = findViewById(R.id.linearRating);

        imgStar1 = findViewById(R.id.imgStar1);
        imgStar2 = findViewById(R.id.imgStar2);
        imgStar3 = findViewById(R.id.imgStar3);
        imgStar4 = findViewById(R.id.imgStar4);
        imgStar5 = findViewById(R.id.imgStar5);
        imgStar6 = findViewById(R.id.imgStar6);
        imgStar7 = findViewById(R.id.imgStar7);
        imgStar8 = findViewById(R.id.imgStar8);
        imgStar9 = findViewById(R.id.imgStar9);
        imgStar10 = findViewById(R.id.imgStar10);

        checkBox = findViewById(R.id.checkBox);

        if (isEditMode) {
            edtRating.setVisibility(View.GONE);
            linearRating.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);
        } else {
            linearRating.setVisibility(View.GONE);
            edtRating.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.INVISIBLE);
        }
    }

    /*Method to validate fields form UI*/
    private boolean validFields() {
        String name = edtName.getText().toString();
        String director = edtDirector.getText().toString();
        String year = edtYear.getText().toString();
        String review = edtReview.getText().toString();
        String rating = edtRating.getText().toString();
        String cast = edtCast.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name must no be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (year.isEmpty()) {
            Toast.makeText(this, "Year must no be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.parseInt(year) <= 1895) {
            Toast.makeText(this, "Year must be greater than 1895", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (director.isEmpty()) {
            Toast.makeText(this, "Director Name must no be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cast.isEmpty()) {
            Toast.makeText(this, "Cast must no be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isEditMode) {
            if (rating.isEmpty()) {
                Toast.makeText(this, "Rating must no be empty", Toast.LENGTH_SHORT).show();
                return false;
            }
            if ((Integer.parseInt(rating) > 10 || Integer.parseInt(rating) < 1)) {
                Toast.makeText(this, "Rating must be between 1 - 10", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (review.isEmpty()) {
            Toast.makeText(this, "Review must no be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}