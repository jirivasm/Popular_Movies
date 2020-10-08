package com.example.popularmovies;

import androidx.room.TypeConverter;

public class Reviews {

    String mAuthor;
    String mContent;

    Reviews(String author,String content)
    {
        mAuthor = author;
        mContent = content;
    }


    public String getAuthor(){return mAuthor;}

    public String getContent(){return mContent;}
}
