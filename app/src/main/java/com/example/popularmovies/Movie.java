package com.example.popularmovies;

public class Movie {

    //what a movie should get
    //Poster
    //Name
    //Release Date
    //Description

    private String mPosterResource;
    private String mMovieName;
    private String mReleaseDate;
    private String mDescription;
    private double mVoteAverage;
    private String mMovieID;

    //Empty Constructor
    Movie(String poster, String name, String release, String description, double average, String id) {
        mPosterResource = poster;
        mMovieName = name;
        mReleaseDate = release;
        mDescription = description;
        mVoteAverage = average;
        mMovieID = id;
    }

    Movie(String poster, String id) {
        mPosterResource = poster;
        mMovieID = id;
    }


    //Getters
    public String getPoster() {
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

    public double getVoterAverage() {
        return mVoteAverage;
    }

    public String getMovieID() {
        return mMovieID;
    }


}
