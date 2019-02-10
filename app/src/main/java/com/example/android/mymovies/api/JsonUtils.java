package com.example.android.mymovies.api;

import com.example.android.mymovies.model.Movie;
import com.example.android.mymovies.model.Review;
import com.example.android.mymovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    private static final String RESULTS = "results";

    //Movies
    private static final String API_MOVIE_ID = "id";
    private static final String API_MOVIE_TITLE = "title";
    private static final String API_MOVIE_RELEASE_DATE = "release_date";
    private static final String API_MOVIE_VOTE_AVERAGE = "vote_average";
    private static final String API_MOVIE_OVERVIEW = "overview";
    private static final String API_MOVIE_POSTER_PATH = "poster_path";

    //Reviews
    private static final String API_REVIEW_AUTHOR = "author";
    private static final String API_REVIEW_CONTENT = "content";

    //Trailers
    private static final String API_TRAILER_ID = "id";
    private static final String API_TRAILER_KEY = "key";
    private static final String API_TRAILER_NAME = "name";
    private static final String API_TRAILER_TYPE = "type";

    public static List<Movie> getMovieStringsFromJson(String movieJsonStr) throws JSONException {
        List<Movie> movieList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(movieJsonStr);

        JSONArray movieArray = jsonObject.getJSONArray(RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject resultsObject = movieArray.getJSONObject(i);

            Movie currentMovie = parseMovie(resultsObject);
            movieList.add(currentMovie);
        }

        return movieList;
    }

    private static Movie parseMovie(JSONObject movie) throws JSONException {
        Movie currentMovie = new Movie();
        currentMovie.setIdFromApi(movie.getInt(API_MOVIE_ID));
        currentMovie.setTitle(movie.optString(API_MOVIE_TITLE));
        currentMovie.setReleaseDate(movie.optString(API_MOVIE_RELEASE_DATE));
        currentMovie.setVoteAverage(movie.optLong(API_MOVIE_VOTE_AVERAGE));
        currentMovie.setOverview(movie.optString(API_MOVIE_OVERVIEW));
        currentMovie.setPosterPath(movie.optString(API_MOVIE_POSTER_PATH));

        return currentMovie;
    }

    public static List<Review> getReviewStringsFromJson(String reviewJsonStr) throws JSONException {
        List<Review> reviewList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(reviewJsonStr);

        JSONArray reviewArray = jsonObject.getJSONArray(RESULTS);

        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject resultObject = reviewArray.getJSONObject(i);

            Review currentReview = parseReview(resultObject);
            reviewList.add(currentReview);
        }

        return reviewList;
    }

    private static Review parseReview(JSONObject review) {
        Review currentReview = new Review();

        currentReview.setAuthor(review.optString(API_REVIEW_AUTHOR));
        currentReview.setContent(review.optString(API_REVIEW_CONTENT));

        return currentReview;
    }

    public static List<Trailer> getTrailerStringFromJson(String trailerJsonStr) throws JSONException {
        List<Trailer> trailerList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(trailerJsonStr);

        JSONArray trailerArray = jsonObject.getJSONArray(RESULTS);

        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject resultObject = trailerArray.getJSONObject(i);

            Trailer currentTrailer = parseTrailer(resultObject);
            trailerList.add(currentTrailer);
        }

        return trailerList;
    }

    private static Trailer parseTrailer(JSONObject trailer) throws JSONException {
        Trailer currentTrailer = new Trailer();
        currentTrailer.setId(trailer.getString(API_TRAILER_ID));
        currentTrailer.setKey(trailer.getString(API_TRAILER_KEY));
        currentTrailer.setName(trailer.optString(API_TRAILER_NAME));
        currentTrailer.setType(trailer.optString(API_TRAILER_TYPE));

        return currentTrailer;
    }
}
