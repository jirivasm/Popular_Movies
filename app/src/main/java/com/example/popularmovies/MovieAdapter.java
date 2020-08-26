package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    List<Movie> mMovieData;
    MovieAdapter(){}

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder{

        public final ImageView mMoviePoster;
        public final TextView mMovieName;
        public final TextView mMovieReleaseDate;
        public final TextView mMovieDescription;

        public MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mMoviePoster      = itemView.findViewById(R.id.iv_image_poster)  ;
            mMovieName        = itemView.findViewById(R.id.tv_movie_name)  ;
            mMovieReleaseDate = itemView.findViewById(R.id.tv_release_date)  ;
            mMovieDescription = itemView.findViewById(R.id.tv_movie_description)  ;
        }
    }
    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {


            Movie Movie = mMovieData.get(position);
            Picasso.get().load(Movie.mPosterResource).into(holder.mMoviePoster);

            holder.mMovieName.setText(Movie.mMovieName);
            holder.mMovieReleaseDate.setText(Movie.mReleaseDate);
            holder.mMovieDescription.setText(Movie.mDescription);

    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();

    }
    public void setMovieData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
