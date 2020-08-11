package com.example.android.mymovies.services.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mymovies.api.JsonUtils;
import com.example.android.mymovies.api.NetworkUtils;
import com.example.android.mymovies.model.Movie;
import com.example.android.mymovies.utils.LoaderDelegate;

import java.net.URL;
import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private LoaderDelegate delegate;

    public static final String MOVIE_FILTER_LIST = "filter_list";
    public static final int MOVIE_LOADER_ID = 20;

    private final String filter;

    public MovieLoader(Context context, LoaderDelegate responder, String filter) {
        super(context);
        this.delegate = responder;
        this.filter = filter;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (delegate != null) delegate.processInitiate(MOVIE_LOADER_ID);

        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        URL moviesRequestUrl = NetworkUtils.buildUrl(filter);

        try {
            String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

            return JsonUtils.getMovieStringsFromJson(jsonMoviesResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(List<Movie> movies) {
        super.deliverResult(movies);
    }
}
