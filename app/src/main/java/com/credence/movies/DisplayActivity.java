package com.credence.movies;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "DisplayActivity";

    private RecyclerView recyclerMovies;
    private MovieListAdapter movieListAdapter;
    private List<Movie> allMovies;
    private boolean showFav = false;
    private boolean isEditMode = false;
    private Context context;
    private boolean isSearchMode = false;
    private EditText edtSearch;
    private boolean isIMDBMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        context = this;

        /*Check if activity should show fav movies only*/
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.containsKey("fav")) {
                    showFav = bundle.getBoolean("fav");
                }
                if (bundle.containsKey("edit_mode")) {
                    isEditMode = bundle.getBoolean("edit_mode");
                }
                if (bundle.containsKey("search_mode")) {
                    isSearchMode = bundle.getBoolean("search_mode");
                }
                if (bundle.containsKey("imdb_mode")) {
                    isIMDBMode = bundle.getBoolean("imdb_mode");
                }
            }
        }

        init();

        findViewById(R.id.btnAddFav).setOnClickListener(view -> {
            if (!isSearchMode) {
                if (movieListAdapter != null) {
                    for (Integer integer : movieListAdapter.movieListToSetFavourite) {
                        Log.d(TAG, "onCreate: " + integer);
                        for (Movie allMovie : allMovies) {
                            if (allMovie.getId() == integer)
                                new DBManager(this).update(allMovie.getId(), allMovie.setFavourite(showFav ? 0 : 1));
                        }
                    }

                    movieListAdapter.movieListToSetFavourite.clear();
                    setUpMovieData();
                }
            } else {
                String searchData = edtSearch.getText().toString();
                if (searchData.isEmpty()) {
                    Toast.makeText(context, "Please enter search text", Toast.LENGTH_SHORT).show();
                } else {
                    getMovieData(searchData);
                    setUpRecycler();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSearchMode)
            setUpMovieData();
    }

    /* Method to get data from database and show in list*/
    private void setUpMovieData() {
        getMovieData("");
        setUpRecycler();
    }

    /*code to set movie data in list*/
    private void setUpRecycler() {
        if (allMovies != null) {
            if (movieListAdapter == null) {
                movieListAdapter = new MovieListAdapter();
                recyclerMovies.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            }
            movieListAdapter.setMovieList(allMovies);
            recyclerMovies.setAdapter(movieListAdapter);
            movieListAdapter.notifyDataSetChanged();
        }
    }

    private void getMovieData(String searchText) {
        final DBManager dbManager = new DBManager(this);
        if (showFav)
            allMovies = dbManager.getFavouriteMovie();
        else if (isSearchMode) {
            allMovies = dbManager.getListOfMovies(searchText);
        } else
            allMovies = dbManager.getAllMovies();
    }

    /* Method to convert UI element to variable*/
    private void init() {
        recyclerMovies = findViewById(R.id.recyclerMovies);
        edtSearch = findViewById(R.id.edtSearch);

        edtSearch.setVisibility(View.GONE);
        if (showFav) ((Button) findViewById(R.id.btnAddFav)).setText("Remove from Favourite");

        if (isIMDBMode)
            findViewById(R.id.btnAddFav).setVisibility(View.GONE);

        if (isSearchMode) {
            ((Button) findViewById(R.id.btnAddFav)).setText("Search Movie");
            edtSearch.setVisibility(View.VISIBLE);
        }
    }

    class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

        List<Movie> movieList;
        List<Integer> movieListToSetFavourite = new ArrayList<>();

        public MovieListAdapter setMovieList(List<Movie> movieList) {
            this.movieList = movieList;
            return this;
        }

        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();
            Movie movie = movieList.get(adapterPosition);

            Log.d(TAG, "onBindViewHolder: " + movie.toString());
            holder.txtName.setText(movie.getName());
            holder.txtDirector.setText(movie.getDirector());
            try {
                holder.txtYear.setText(String.valueOf(movie.getYear()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.txtCast.setText(movie.getCast());
            try {
                holder.txtRating.setText(String.valueOf(movie.getRating()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.txtReview.setText(movie.getReview());
            holder.checkbox.setChecked(movieListToSetFavourite.contains(adapterPosition));

            holder.checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
                Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
                if (b) {
                    movieListToSetFavourite.add(movie.getId());
                } else {
                    if (movieListToSetFavourite.contains(movie.getId()))
                        movieListToSetFavourite.remove(movie.getId());
                }
            });

            holder.txtName.setOnClickListener(view -> {
                if (isIMDBMode) {
                    makeAPICall(movie.getName());
                } else if (isEditMode) {
                    startActivity(new Intent(context, RegisterActivity.class)
                            .putExtra("edit_mode", true)
                            .putExtra("movie_index", movie.getId()));
                }
            });

        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }

        public class MovieViewHolder extends RecyclerView.ViewHolder {

            private final TextView txtName, txtDirector, txtYear, txtRating, txtCast, txtReview;
            private final CheckBox checkbox;

            public MovieViewHolder(@NonNull View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.txtName);
                txtDirector = itemView.findViewById(R.id.txtDirector);
                txtYear = itemView.findViewById(R.id.txtYear);
                txtRating = itemView.findViewById(R.id.txtRating);
                txtReview = itemView.findViewById(R.id.txtReview);
                txtCast = itemView.findViewById(R.id.txtCast);
                checkbox = itemView.findViewById(R.id.checkbox);
            }
        }
    }

    /*API call for IMDB data*/
    private void makeAPICall(String name) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting data.. please wait");
        progressDialog.show();

        Log.d(TAG, "makeAPICall() called with: name = [" + name + "]");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, String.format("https://imdb-api.com/en/API/SearchTitle/k_67vjodhq/%s", name), response -> {
            Log.d(TAG, "makeAPICall() called with: name = [" + name + "]");
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject data = results.getJSONObject(i);

                    progressDialog.dismiss();
                    getMovieRatingFromIMDB(data.getString("id"), data.getString("image"));
                    Log.d(TAG, "onResponse: " + data.get("id"));
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getMovieRatingFromIMDB(String id, String imageURL) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting data.. please wait");
        progressDialog.show();

        Log.d(TAG, "getMovieRatingFromIMDB() called with: id = [" + id + "]");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, String.format("https://imdb-api.com/en/API/UserRatings/k_67vjodhq/%s", id), response -> {
            Log.d(TAG, "getMovieRatingFromIMDB() called with: id = [" + id + "]");
            try {
                JSONObject jsonObject = new JSONObject(response);
                String rating = jsonObject.getString("totalRating");
                progressDialog.dismiss();

                showIMDBDialog(rating, imageURL);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void showIMDBDialog(String rating, String imageURL) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_imdb_data);

        ImageView imgBanner = dialog.findViewById(R.id.imgBanner);
        TextView txtIMDBRating = dialog.findViewById(R.id.txtIMDBRating);

        txtIMDBRating.setText(rating.isEmpty() ? "Data is empty, try another movie" : rating);

        new Thread(() -> {
            try {
                URL url = new URL(imageURL);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                runOnUiThread(() -> imgBanner.setImageBitmap(bmp));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        dialog.show();
    }
}