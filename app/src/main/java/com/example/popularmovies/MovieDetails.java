package com.example.popularmovies;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetails extends AppCompatActivity {

    private static final String SCHEME = "https";
    private static final String BASE_MOVIE_URL = "api.themoviedb.org";
    private static final String BASE_MOVIE_URL_VERSION = "3";
    private static final String BASE_MOVIE_URL_VIDEO_TYPE = "movie";
    //TODO:2  Put your key here
    private static final String API_KEY = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent getinfoIntent = getIntent();
        Bundle bundle = getinfoIntent.getExtras();

        String MovieID = "";
        if(bundle != null)
            MovieID = bundle.getString(getString(R.string.movieID));


        LoadMovieDetails(MovieID);
    }

    public static String buildUrl(String movieID) {
        Uri.Builder builtUri = new Uri.Builder();

        builtUri.scheme(SCHEME);
        builtUri.authority(BASE_MOVIE_URL);
        builtUri.appendPath(BASE_MOVIE_URL_VERSION);
        builtUri.appendPath(BASE_MOVIE_URL_VIDEO_TYPE);
        builtUri.appendPath(movieID);
        builtUri.appendQueryParameter("api_key",API_KEY);

        return builtUri.toString();

    }
    private void showMoviesDataView() {

        TextView error = findViewById(R.id.tv_error_message_details);
        ProgressBar progress = findViewById(R.id.pb_loading_indicator_detail);
        error.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage() {

        TextView error = findViewById(R.id.tv_error_message_details);
        ProgressBar progress = findViewById(R.id.pb_loading_indicator_detail);
        error.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
    }
    private void LoadMovieDetails(String movieID) {

        String urlBuilder = buildUrl(movieID);
        FetchMovieTask task = new FetchMovieTask();
        task.execute(urlBuilder);
    }
    public class FetchMovieTask extends AsyncTask<String, Void, Movie> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progress = findViewById(R.id.pb_loading_indicator_detail);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);

            if(movie!=null) {
                ProgressBar progress = findViewById(R.id.pb_loading_indicator_detail);
                progress.setVisibility(View.INVISIBLE);

                ImageView imagePoster = findViewById(R.id.iv_detail_poster);
                TextView titleTextView = findViewById(R.id.tv_movie_title);
                TextView releaseDateTextView = findViewById(R.id.tv_release_date);
                TextView ratingTextView = findViewById(R.id.tv_rating);
                TextView descriptionTextView = findViewById(R.id.tv_description);


                Picasso.get().load(movie.getPoster()).into(imagePoster);
                titleTextView.setText(movie.getMovieName());
                releaseDateTextView.setText(movie.getReleaseDate());
                ratingTextView.setText(String.valueOf(movie.getVoterAverage()));
                descriptionTextView.setText(movie.getDescription());
            }
            else
                showErrorMessage();

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Movie doInBackground(String... strings) {
            if (strings[0].isEmpty())
                return null;

            String movieUrl = strings[0];

            try {
                return MovieNetworkUtils.fetchMovieDetails(movieUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}