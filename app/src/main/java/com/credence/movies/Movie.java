package com.credence.movies;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.credence.movies.DatabaseHelper.CAST;
import static com.credence.movies.DatabaseHelper.DIRECTOR;
import static com.credence.movies.DatabaseHelper.FAVOURITE;
import static com.credence.movies.DatabaseHelper.MOVIE_NAME;
import static com.credence.movies.DatabaseHelper.RATING;
import static com.credence.movies.DatabaseHelper.REVIEW;
import static com.credence.movies.DatabaseHelper.YEAR;

public class Movie {
                                //Setters and Getters
    private int id;
    private String name;
    private String review;
    private String cast;
    private String director;
    private Integer rating;
    private Integer year;
    private Integer favourite = 0;

    public int getId() {
        return id;
    }

    public Movie setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Movie setName(String name) {
        this.name = name;
        return this;
    }



    public String getReview() {
        return review;
    }

    public Movie setReview(String review) {
        this.review = review;
        return this;
    }

    public String getCast() {
        return cast;
    }

    public Movie setCast(String cast) {
        this.cast = cast;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public Movie setDirector(String director) {
        this.director = director;
        return this;
    }


    public Integer getRating() {
        return rating;
    }

    public Movie setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public Movie setYear(Integer year) {
        this.year = year;
        return this;
    }

    public Integer getFavourite() {
        return favourite;
    }

    public Movie setFavourite(Integer favourite) {
        this.favourite = favourite;
        return this;
    }

    public boolean isFavourite() {
        return favourite == 1;
    }

    public List<String> getListOfCast() {
        return new ArrayList<String>() {{
            addAll(Arrays.asList(cast.split(",")));
        }};
    }

    public ContentValues getContentValues() {
        final ContentValues contentValue = new ContentValues();
        contentValue.put(MOVIE_NAME, name);
        contentValue.put(YEAR, year);
        contentValue.put(DIRECTOR, director);
        contentValue.put(CAST, cast);
        contentValue.put(RATING, rating);
        contentValue.put(REVIEW, review);
        contentValue.put(FAVOURITE, favourite);
        return contentValue;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", review='" + review + '\'' +
                ", cast='" + cast + '\'' +
                ", director='" + director + '\'' +
                ", rating=" + rating +
                ", year=" + year +
                ", favourite=" + favourite +
                '}';
    }
}
