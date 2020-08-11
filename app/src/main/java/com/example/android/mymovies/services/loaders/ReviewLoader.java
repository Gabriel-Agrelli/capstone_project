package com.example.android.mymovies.services.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.mymovies.api.JsonUtils;
import com.example.android.mymovies.api.NetworkUtils;
import com.example.android.mymovies.model.Review;
import com.example.android.mymovies.utils.LoaderDelegate;

import java.net.URL;
import java.util.List;

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {
    private LoaderDelegate delegate;

    public static final String MOVIE_ID = "id";
    public static final int REVIEW_LOADER_ID = 21;

    private final Integer movieId;

    public ReviewLoader(Context context, LoaderDelegate responder, int movieId) {
        super(context);
        this.delegate = responder;
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (delegate != null) delegate.processInitiate(REVIEW_LOADER_ID);

        forceLoad();
    }

    @Override
    public List<Review> loadInBackground() {
        if (movieId == null) return null;

        String reviewsPath = movieId + "/reviews";
        URL reviewsRequestUrl = NetworkUtils.buildUrl(reviewsPath);

        try {
            String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);

            return JsonUtils.getReviewStringsFromJson(jsonReviewResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(List<Review> reviews) {
        super.deliverResult(reviews);
    }
}
