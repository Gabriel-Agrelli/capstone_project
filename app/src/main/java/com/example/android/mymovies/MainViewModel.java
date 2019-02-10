package com.example.android.mymovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.mymovies.database.AppDatabase;
import com.example.android.mymovies.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> favoritesMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favoritesMovies = database.favoritesMoviesDAO().loadAllFavorites();
    }

    public LiveData<List<Movie>> getFavoritesMovies() {
        return favoritesMovies;
    }
}
