package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity {


    boolean isByPopularity = true;

    private static final String SCHEME = "https";
    private static final String BASE_MOVIE_URL = "api.themoviedb.org";
    private static final String BASE_MOVIE_URL_PATH1 = "3";
    private static final String BASE_MOVIE_URL_PATH2 = "movie";
    private static final String SORT_BY_POPULARITY = "popular";
    private static final String SORT_BY_RATING = "top_rated";
    private static final String API_KEY = "32d1f41681e20fadbc912d5f398a3617";


    private RecyclerView mRecyclerView;
    // COMPLETED (35) Add a private ForecastAdapter variable called mForecastAdapter
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
            item.setTitle("Rating");
            isByPopularity = false;
        } else {
            item.setTitle("Popularity");
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


        mMovieAdapter = new MovieAdapter();

        mRecyclerView.setAdapter(mMovieAdapter);


        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData();
    }

    private void loadMovieData() {
        String urlbuilder;
        if(isByPopularity)
            urlbuilder =  buildUrl(SORT_BY_POPULARITY);
        else
            urlbuilder =  buildUrl(SORT_BY_RATING);

        FetchMovieTask task = new FetchMovieTask();
        task.execute(urlbuilder);
    }
    public static String buildUrl(String popularityOrRating) {
        Uri.Builder builtUri = new Uri.Builder();

        builtUri.scheme(SCHEME);
        builtUri.authority(BASE_MOVIE_URL);
        builtUri.appendPath(BASE_MOVIE_URL_PATH1);
        builtUri.appendPath(BASE_MOVIE_URL_PATH2);
        builtUri.appendPath(popularityOrRating);
        builtUri.appendQueryParameter("api_key",API_KEY);

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
    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

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
                return MovieNetworkUtils.fetchMovieData(movieUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}