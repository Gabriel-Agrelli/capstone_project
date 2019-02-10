package com.example.android.mymovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.mymovies.model.Movie;

import java.util.List;

@Dao
public interface FavoritesMoviesDAO {

    @Query("SELECT * FROM favorites ORDER BY updated_at")
    LiveData<List<Movie>> loadAllFavorites();

    @Insert
    void insertFavoriteMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(Movie movie);

    @Delete
    void deleteFavoriteMovie(Movie movie);

    @Query("SELECT * FROM favorites WHERE idFromApi = :idFromApi")
    Movie loadFavoriteMovieById(int idFromApi);
}
