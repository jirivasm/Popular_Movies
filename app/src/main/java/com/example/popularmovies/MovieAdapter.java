package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    List<Movie> mMovieData;

    public final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movieSelected);
    }

    MovieAdapter(MovieAdapterOnClickHandler onClickHandler) {
       mClickHandler = onClickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

        public final ImageView mMoviePoster;


        public MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mMoviePoster = itemView.findViewById(R.id.iv_image_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movieSelected = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movieSelected);
        }
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);


        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {


        Movie Movie = mMovieData.get(position);
        Picasso.get().load(Movie.getPoster()).into(holder.mMoviePoster);

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
