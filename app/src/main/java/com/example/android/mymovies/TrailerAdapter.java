package com.example.android.mymovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mymovies.model.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<Trailer> mTrailersData = new ArrayList<>();
    private OnTrailerItemClick onTrailerItemClick;

    public TrailerAdapter(OnTrailerItemClick onTrailerItemClick) {
        this.onTrailerItemClick = onTrailerItemClick;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.list_trailers, parent, false);
        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);

        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bind(mTrailersData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mTrailersData == null) return 0;
        return mTrailersData.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTxtName;
        private Trailer trailer;
        private View view;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            mTxtName = itemView.findViewById(R.id.txt_trailer_name);
        }

        void bind(Trailer trailer) {
            this.trailer = trailer;
            mTxtName.setText(trailer.getName());
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTrailerItemClick.onTrailerItemClick(trailer);
        }
    }

    void setTrailers(List<Trailer> trailers) {
        this.mTrailersData = trailers;
        notifyDataSetChanged();
    }

    public interface OnTrailerItemClick {
        void onTrailerItemClick(Trailer trailer);
    }
}
