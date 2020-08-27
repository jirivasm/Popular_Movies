package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
implements MovieAdapter.MovieAdapterOnClickHandler {


    boolean isByPopularity = true;

    private static final String SCHEME = "https";
    private static final String BASE_MOVIE_URL = "api.themoviedb.org";
    private static final String BASE_MOVIE_URL_VERSION = "3";
    private static final String BASE_MOVIE_URL_VIDEO_TYPE = "movie";
    private static final String SORT_BY_POPULARITY = "popular";
    private static final String SORT_BY_RATING = "top_rated";
    //TODO:1 Put your key here
    private static final String API_KEY = "32d1f41681e20fadbc912d5f398a3617";


    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        isByPopularity = true;
        getMenuInflater().inflate(R.menu.sort_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isByPopularity) {
            item.setTitle(getResources().getString(R.string.rating));
            isByPopularity = false;
        } else {
           item.setTitle(getResources().getString(R.string.popularity));
            isByPopularity = true;
        }
        loadMovieData();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_list_recycled);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);


        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData();
    }

    private void loadMovieData() {
        showMoviesDataView();

        String urlBuilder;
        if(isByPopularity)
            urlBuilder =  buildUrl(SORT_BY_POPULARITY);
        else
            urlBuilder =  buildUrl(SORT_BY_RATING);

        FetchMovieTask task = new FetchMovieTask();
        task.execute(urlBuilder);
    }
    public static String buildUrl(String popularityOrRating) {
        Uri.Builder builtUri = new Uri.Builder();

        builtUri.scheme(SCHEME);
        builtUri.authority(BASE_MOVIE_URL);
        builtUri.appendPath(BASE_MOVIE_URL_VERSION);
        builtUri.appendPath(BASE_MOVIE_URL_VIDEO_TYPE);
        builtUri.appendPath(popularityOrRating);
        builtUri.appendQueryParameter( "api_key" ,API_KEY);

        return builtUri.toString();

    }
    private void showMoviesDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage() {

        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movieSelected) {

        Intent intentToShowMovieDetails = new Intent(this,MovieDetails.class);
        intentToShowMovieDetails.putExtra(getString(R.string.movieID),movieSelected.getMovieID());
        startActivity(intentToShowMovieDetails);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showMoviesDataView();
                 mMovieAdapter.setMovieData(movies);
            } else {
                showErrorMessage();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected List<Movie> doInBackground(String... url) {

            if (url[0].isEmpty())
                return null;

            String movieUrl = url[0];

            try {
                return MovieNetworkUtils.fetchMovieList(movieUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}