package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    List<String> mTrailerData;

    public final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailerSelected);
    }

    TrailerAdapter(TrailerAdapterOnClickHandler onClickHandler) {
        mClickHandler = onClickHandler;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener
    {
        private final Button mTrailerButton;

        public TrailerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mTrailerButton = itemView.findViewById(R.id.trailerButton);
            mTrailerButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           int adapterPosition = getAdapterPosition();
           String trailerSelected = mTrailerData.get(adapterPosition);
           mClickHandler.onClick(trailerSelected);
        }

    }
    @NonNull
    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);


        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerAdapterViewHolder holder, int position) {

        String trailerData = mTrailerData.get(position);
        holder.mTrailerButton.setText("Trailer #" + (position+1));
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData) return 0;
        return mTrailerData.size();
    }

    public void setTrailerData(List<String> TrailerData) {
        mTrailerData = TrailerData;
        notifyDataSetChanged();
    }

}


