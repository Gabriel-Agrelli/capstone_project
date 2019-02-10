package com.example.android.mymovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mymovies.api.NetworkUtils;
import com.example.android.mymovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends BaseAdapter {
    private final Context context;
    private List<Movie> mMoviesData = new ArrayList<>();

    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public int getCount() {
        if (mMoviesData == null) return 0;
        return mMoviesData.size();
    }

    @Override
    public Object getItem(int position) {
        return mMoviesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TextView mTxtMovieTitle;
        final ImageView mImgMovie;
        View gridView = view;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.list_movies, null);
        }

        mTxtMovieTitle = gridView.findViewById(R.id.txt_movie_title);
        mImgMovie = gridView.findViewById(R.id.img_movie);

        Picasso.get().load(NetworkUtils.getPosterUrl(mMoviesData.get(position).getPosterPath())).into(mImgMovie);
        mTxtMovieTitle.setText(String.valueOf(mMoviesData.get(position).getTitle()));

        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHandler.onClick(mMoviesData.get(position));
            }
        });

        return gridView;
    }

    public void setMovies(List<Movie> moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}
