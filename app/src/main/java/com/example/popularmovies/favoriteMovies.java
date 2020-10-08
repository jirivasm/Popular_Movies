package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;


public class favoriteMovies extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private MovieDataBase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);

        mRecyclerView = findViewById(R.id.favorite_movie_list_recycled);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);


        mDB = MovieDataBase.getInstance(getApplicationContext());
        setUpViewModel();
    }

    private void setUpViewModel() {

       ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
       viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mMovieAdapter.setMovieData(movies);
            }
        });
    }
    @Override
    public void onClick(Movie movieSelected) {

        Intent intentToShowMovieDetails = new Intent(this, MovieDetails.class);
        intentToShowMovieDetails.putExtra(getString(R.string.movieID), movieSelected.getMovieID());
        intentToShowMovieDetails.putExtra(getString(R.string.isChecked),true);
        startActivity(intentToShowMovieDetails);
    }
}