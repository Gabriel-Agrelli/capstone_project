package com.example.android.mymovies.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.android.mymovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String API_KEY = BuildConfig.api_key;
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";

    public static URL buildUrl(String parameter) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(parameter)
                .appendQueryParameter("api_key", API_KEY)
                .build();

        URL url = null;
        String decodeUrl = null;

        try {
            decodeUrl = URLDecoder.decode(builtUri.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            url = new URL(decodeUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static String getPosterUrl(String posterPath) {
        return "http://image.tmdb.org/t/p/w500" + posterPath;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String buildYoutubeUrl(String key) {
        return YOUTUBE_BASE_URL.concat(key);
    }
}
