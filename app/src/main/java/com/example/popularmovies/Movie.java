package com.example.popularmovies;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.List;

@Entity(tableName = "favoritemovies")
public class Movie {


    private String mPosterResource;
    private String mMovieName;
    private String mReleaseDate;
    private String mDescription;
    private double mVoteAverage;
    @Ignore
    private List<String> mTrailers;

    @Ignore
    private List<Reviews> mReviews;

    @PrimaryKey(autoGenerate = true)
    private int mMovieID;
    private boolean mIsFavorite;


    @Ignore
    Movie(String posterResource, String movieName, String releaseDate, String description, double voteAverage, int movieID, boolean isFavorite, List<Reviews> reviews, List<String> trailers) {
        mPosterResource = posterResource;
        mMovieName = movieName;
        mReleaseDate = releaseDate;
        mDescription = description;
        mVoteAverage = voteAverage;
        mMovieID = movieID;
        mIsFavorite = isFavorite;

        mReviews = reviews;
        mTrailers = trailers;
    }


    Movie(String posterResource, int movieID) {
        mPosterResource = posterResource;
        mMovieID = movieID;

    }

    //Getters
    public String getPosterResource() {
        return mPosterResource;
    }

    public String getMovieName() {
        return mMovieName;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public int getMovieID() {
        return mMovieID;
    }

    public boolean getIsFavorite() {
        return mIsFavorite;
    }

    public List<Reviews> getmReviews() {
        return mReviews;
    }

    public List<String> getTrailers() {return mTrailers; }

    //Setters


    public void setTrailers(List<String> mTrailers) {
        this.mTrailers = mTrailers;
    }

    public void setIsFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    public void setPosterResource(String posterResource) {
        mPosterResource = posterResource;
    }

    public void setMovieName(String name) {
        mMovieName = name;
    }

    public void setReleaseDate(String date) {
        mReleaseDate = date;
    }

    public void setVoteAverage(double average) {
        mVoteAverage = average;
    }

    public void setDescription(String desc) {
        mDescription = desc;
    }

    public void setMovieID(int id) {
        mMovieID = id;
    }

    public void setmReviews(List<Reviews> mReviews) {
        mReviews = mReviews;
    }


}
