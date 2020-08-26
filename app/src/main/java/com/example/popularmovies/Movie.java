package com.example.popularmovies;

public class Movie {

    //what a movie should get
    //Poster
    //Name
    //Release Date
    //Description

    String mPosterResource;
    String mMovieName;
    String mReleaseDate;
    String mDescription;
    double mVoteAverage;
    int    mMovieID;
    //Empty Constructor
    Movie(String poster, String name, String release, String description,double average,int id){
        mPosterResource= poster;
        mMovieName     = name;
        mReleaseDate   = release;
        mDescription   = description;
        mVoteAverage   = average;
        mMovieID       = id;
    }
    Movie(){}



    //Getters
   public String getPoster(){return mPosterResource;}
   public String getMovieName(){return mMovieName;}
   public String getReleaseDate(){return mReleaseDate;}
   public String getDescription(){return  mDescription;}
   public double getVoterAverage(){return mVoteAverage;}
   public int getMovieID(){return mMovieID;}


    //Setters
   public void setPosterResource(String resource){mPosterResource = resource;}
   public void setMovieName(String name){mMovieName = name;}
   public void setReleaseDate(String releaseDate){mReleaseDate = releaseDate;}
   public void setDescription(String description){mDescription = description;}
   public void setVoterAverage(double average){mVoteAverage = average;}
   public void setMovieID(int id){mMovieID=id;}

}
