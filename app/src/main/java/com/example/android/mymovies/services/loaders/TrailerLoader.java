package com.example.android.mymovies.services.loaders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mymovies.api.JsonUtils;
import com.example.android.mymovies.api.NetworkUtils;
import com.example.android.mymovies.model.Trailer;
import com.example.android.mymovies.utils.LoaderDelegate;

import java.net.URL;
import java.util.List;

public class TrailerLoader extends AsyncTaskLoader<List<Trailer>> {
    private LoaderDelegate delegate;

    public static final String MOVIE_ID = "id";
    public static final int TRAILER_LOADER_ID = 22;

    private final Integer movieId;

    public TrailerLoader(Context context, LoaderDelegate responder, int movieId) {
        super(context);
        this.delegate = responder;
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (delegate != null) delegate.processInitiate(TRAILER_LOADER_ID);

        forceLoad();
    }

    @Nullable
    @Override
    public List<Trailer> loadInBackground() {
        if (movieId == null) return null;

        String trailersPath = movieId + "/videos";
        URL reviewsRequestUrl = NetworkUtils.buildUrl(trailersPath);

        try {
            String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);

            return JsonUtils.getTrailerStringFromJson(jsonTrailerResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable List<Trailer> trailers) {
        super.deliverResult(trailers);
    }
}
