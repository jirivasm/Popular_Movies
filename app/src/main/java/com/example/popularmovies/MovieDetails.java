package com.example.popularmovies;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetails extends AppCompatActivity
implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String SCHEME = "https";
    private static final String BASE_MOVIE_URL = "api.themoviedb.org";
    private static final String BASE_MOVIE_URL_VERSION = "3";
    private static final String BASE_MOVIE_URL_VIDEO_TYPE = "movie";
    private static final String BASE_MOVIE_URL_REVIEWS = "reviews";
    private static final String BASE_MOVIE_URL_TRAILERS = "trailers";
    private static final String YOUTUBE_BASE_URL = "https://youtube.com/watch?v=";

    //TODO:2  Put your key here
    private static final String API_KEY = "";

    private RecyclerView mReviewRecyclerView;
    private RecyclerView mTrailerRecyclerView;

    Movie currMovie = null;
    private MovieDataBase mDB;
    ReviewAdapter mReviewAdapter;
    TrailerAdapter mTrailerAdapter;
    TextView mNoReviewsTextView;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        mDB = MovieDataBase.getInstance(getApplicationContext());

        Intent getinfoIntent = getIntent();
        Bundle bundle = getinfoIntent.getExtras();

        int MovieID = 0;

        if (bundle != null) {
            MovieID = bundle.getInt(getString(R.string.movieID));

        }


        final CheckBox favoritesCheckBox = findViewById(R.id.check_box_favorite_star);

        favoritesCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!favoritesCheckBox.isChecked())
                {
                    favoritesCheckBox.setChecked(false);
                    RemoveFromFavorites();
                }
                else
                {
                    favoritesCheckBox.setChecked(true);
                    AddToFavorites();
                }

            }
        });
        mNoReviewsTextView = findViewById(R.id.no_reviews_available);
        mReviewRecyclerView = findViewById(R.id.recycle_reviews);
        mTrailerRecyclerView = findViewById(R.id.recycled_trailers);

        LinearLayoutManager ReviewlinearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager TrailerlinearLayoutManager = new LinearLayoutManager(this);


        mReviewRecyclerView.setLayoutManager(ReviewlinearLayoutManager);
        mTrailerRecyclerView.setLayoutManager(TrailerlinearLayoutManager);

        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        LoadMovieDetails(MovieID);
    }

    public void AddToFavorites() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                if (!currMovie.getIsFavorite()) {
                    mDB.movieDao().insertMovie(currMovie);
                    currMovie.setIsFavorite(true);
                    finish();
                }
            }
        });
    }

    public void RemoveFromFavorites() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {

            @Override
            public void run() {
                if (currMovie.getIsFavorite()) {
                    mDB.movieDao().deleteMovie(currMovie);
                    currMovie.setIsFavorite(false);
                    finish();
                }
            }
        });

    }

    public static String[] buildUrl(String movieID) {
        Uri.Builder builtUriDetails = new Uri.Builder();
        Uri.Builder builtUriReviews = new Uri.Builder();
        Uri.Builder buildUriTrailers = new Uri.Builder();

        builtUriDetails.scheme(SCHEME);
        builtUriDetails.authority(BASE_MOVIE_URL);
        builtUriDetails.appendPath(BASE_MOVIE_URL_VERSION);
        builtUriDetails.appendPath(BASE_MOVIE_URL_VIDEO_TYPE);
        builtUriDetails.appendPath(movieID);
        builtUriDetails.appendQueryParameter("api_key", API_KEY);

        //Reviews
        builtUriReviews.scheme(SCHEME);
        builtUriReviews.authority(BASE_MOVIE_URL);
        builtUriReviews.appendPath(BASE_MOVIE_URL_VERSION);
        builtUriReviews.appendPath(BASE_MOVIE_URL_VIDEO_TYPE);
        builtUriReviews.appendPath(movieID);
        builtUriReviews.appendPath(BASE_MOVIE_URL_REVIEWS);
        builtUriReviews.appendQueryParameter("api_key", API_KEY);

        //Trailers
        buildUriTrailers.scheme(SCHEME);
        buildUriTrailers.authority(BASE_MOVIE_URL);
        buildUriTrailers.appendPath(BASE_MOVIE_URL_VERSION);
        buildUriTrailers.appendPath(BASE_MOVIE_URL_VIDEO_TYPE);
        buildUriTrailers.appendPath(movieID);
        buildUriTrailers.appendPath(BASE_MOVIE_URL_TRAILERS);
        buildUriTrailers.appendQueryParameter("api_key", API_KEY);

        String [] urls= { builtUriDetails.toString(),builtUriReviews.toString(),buildUriTrailers.toString()};

        return urls;

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

    private void LoadMovieDetails(int movieID) {

        String id = String.valueOf(movieID);

        String[] urlBuilderDetails = buildUrl(id);
        FetchMovieTask task = new FetchMovieTask();
        task.execute(urlBuilderDetails);
    }

    @Override
    public void onClick(String trailerSelected) {

        String trailerKey = trailerSelected;
        Intent goToTrailer = new Intent(Intent.ACTION_VIEW,Uri.parse(YOUTUBE_BASE_URL + trailerKey));
       startActivity(goToTrailer);
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

        if (movie != null) {
            ProgressBar progress = findViewById(R.id.pb_loading_indicator_detail);
            progress.setVisibility(View.INVISIBLE);

            LinearLayout detailsSection = findViewById(R.id.details_section);
            detailsSection.setVisibility(View.VISIBLE);

            ImageView imagePoster = findViewById(R.id.iv_detail_poster);
            TextView titleTextView = findViewById(R.id.tv_movie_title);
            TextView releaseDateTextView = findViewById(R.id.tv_release_date);
            TextView ratingTextView = findViewById(R.id.tv_rating);
            TextView descriptionTextView = findViewById(R.id.tv_description);
            CheckBox favoriteCheckBox = findViewById(R.id.check_box_favorite_star);


            if(movie.getmReviews().size()>0) {
                mReviewAdapter = new ReviewAdapter();
                mReviewAdapter.setReviewData(movie.getmReviews());
                mReviewRecyclerView.setAdapter(mReviewAdapter);
               mNoReviewsTextView.setVisibility(View.GONE);
               mReviewRecyclerView.setVisibility(View.VISIBLE);
            }
            else
            {
                mNoReviewsTextView.setVisibility(View.VISIBLE);
                mReviewRecyclerView.setVisibility(View.GONE);
            }


            mTrailerAdapter.setTrailerData(movie.getTrailers());



            Intent getinfoIntent = getIntent();
            Bundle bundle = getinfoIntent.getExtras();


            if (bundle != null) {
                if(bundle.getBoolean(getString(R.string.isChecked))) {
                    movie.setIsFavorite(true);

                }
                else {
                    movie.setIsFavorite(false);
                    favoriteCheckBox.setChecked(false);
                }
            }



            Picasso.get().load(movie.getPosterResource()).into(imagePoster);
            titleTextView.setText(movie.getMovieName());
            releaseDateTextView.setText(movie.getReleaseDate());
            ratingTextView.setText(String.valueOf(movie.getVoteAverage()));
            descriptionTextView.setText(movie.getDescription());

            currMovie = movie;


            if(currMovie.getIsFavorite())
                favoriteCheckBox.setChecked(true);
            else
                favoriteCheckBox.setChecked(false);

        } else
            showErrorMessage();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Movie doInBackground(String... strings) {

        showMoviesDataView();
        if (strings[0].isEmpty())
            return null;

        String movieUrl = strings[0];
        String reviewUrl = strings[1];
        String trailerUrl = strings[2];

        try {
            return MovieNetworkUtils.fetchMovieDetails(movieUrl,reviewUrl,trailerUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
}