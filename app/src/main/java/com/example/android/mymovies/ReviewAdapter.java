package com.example.android.mymovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mymovies.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> mReviewsData = new ArrayList<>();

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.list_reviews, parent, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);

        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(mReviewsData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mReviewsData == null) return 0;
        return mReviewsData.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtAuthor, mTxtContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            mTxtAuthor = itemView.findViewById(R.id.txt_review_author);
            mTxtContent = itemView.findViewById(R.id.txt_review_content);
        }

        void bind(Review review) {
            mTxtAuthor.setText(review.getAuthor());
            mTxtContent.setText(review.getContent());
        }
    }

    void setReviews(List<Review> reviews) {
        this.mReviewsData = reviews;
        notifyDataSetChanged();
    }
}
