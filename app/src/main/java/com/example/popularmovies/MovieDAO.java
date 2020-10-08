package com.example.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("Select * FROM favoritemovies")
    LiveData<List<Movie>> loadAllMovies();
    @Insert
    void insertMovie(Movie movie);

    @Update (onConflict = OnConflictStrategy.REPLACE )
    void updateMovies(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM FAVORITEMOVIES WHERE mMovieID = :id")
    LiveData<Movie> loadMovieById(int id);
}
