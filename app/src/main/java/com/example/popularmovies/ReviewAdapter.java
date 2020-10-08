package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    List<Reviews> mReviewData;


    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView mContent;
        private final TextView mAuthor;


        public ReviewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mContent = itemView.findViewById(R.id.review_item);
            mAuthor = itemView.findViewById(R.id.review_Author);
        }
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.reviews_item;
        LayoutInflater inflater = LayoutInflater.from(context);


        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {

        Reviews review = mReviewData.get(position);
        holder.mAuthor.setText(review.mAuthor);
        holder.mContent.setText(review.mContent);


    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.size();
    }

    public void setReviewData(List<Reviews> reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}
