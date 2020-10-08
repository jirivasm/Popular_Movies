package com.example.popularmovies;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = ViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movies;

    public ViewModel(Application application) {
        super(application);
        // COMPLETED (4) In the constructor use the loadAllTasks of the taskDao to initialize the tasks variable
        MovieDataBase database = MovieDataBase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        movies = database.movieDao().loadAllMovies();
    }

    // COMPLETED (3) Create a getter for the tasks variable
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}