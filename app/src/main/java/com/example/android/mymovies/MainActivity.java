package com.example.android.mymovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.mymovies.api.NetworkUtils;
import com.example.android.mymovies.model.Movie;
import com.example.android.mymovies.services.loaders.MovieLoader;
import com.example.android.mymovies.utils.LoaderDelegate;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler,
        LoaderDelegate {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private LoaderManager.LoaderCallbacks<List<Movie>> movieLoaderCallback;

    private TextView mTxtErrorFetchData, mTxtSadFace, mTxtTryAgain;
    private ProgressBar mMoviesProgressBar;
    private MoviesAdapter mMoviesAdapter;
    private GridView mRecyclerView;
    private LinearLayout mLayoutWithoutNetwork;

    private static final String NOW_PLAYING = "now_playing";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String FAVORITES = "favorites";
    public static final String CHOOSED_FILTER = "mChoosedFilter";
    private static final String MOVIE_FILTER_LIST = "filter_list";

    private String mChoosedFilter = NOW_PLAYING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtErrorFetchData = findViewById(R.id.txt_error_fetch_data);
        mTxtSadFace = findViewById(R.id.txt_sad_face);
        mTxtTryAgain = findViewById(R.id.txt_try_again);
        mMoviesProgressBar = findViewById(R.id.pb_list_movies);
        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mLayoutWithoutNetwork = findViewById(R.id.layout_without_network);

        mMoviesAdapter = new MoviesAdapter(this, this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        mTxtTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovies(NOW_PLAYING);
            }
        });

        setupLoader();

        if (savedInstanceState != null && savedInstanceState.containsKey(CHOOSED_FILTER)) {
            mChoosedFilter = savedInstanceState.getString(CHOOSED_FILTER);
            loadMovies(mChoosedFilter);
        } else {
            loadMovies(NOW_PLAYING);
        }
    }

    private void setupLoader() {
        movieLoaderCallback = new LoaderManager.LoaderCallbacks<List<Movie>>() {
            @NonNull
            @Override
            public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
                if (args == null) return null;
                String filter = args.getString(MOVIE_FILTER_LIST);
                return new MovieLoader(MainActivity.this, MainActivity.this, filter);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
                if (data != null && !data.equals("")) {
                    mMoviesProgressBar.setVisibility(View.INVISIBLE);
                    mMoviesAdapter.setMovies(data);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
            }
        };
    }

    private void loadMovies(String filter) {
        if (filter == FAVORITES) {
            MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            mainViewModel.getFavoritesMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    mMoviesAdapter.setMovies(movies);
                }
            });
            return;
        }

        if (NetworkUtils.isNetworkConnected(this)) {
            showData();

            Loader loader = getSupportLoaderManager().getLoader(MovieLoader.MOVIE_LOADER_ID);

            Bundle queryBundle = new Bundle();
            queryBundle.putString(MovieLoader.MOVIE_FILTER_LIST, filter);

            if (loader == null) {
                getSupportLoaderManager().initLoader(MovieLoader.MOVIE_LOADER_ID, queryBundle, movieLoaderCallback);
            } else {
                getSupportLoaderManager().restartLoader(MovieLoader.MOVIE_LOADER_ID, queryBundle, movieLoaderCallback);
            }
        } else {
            mTxtErrorFetchData.setText(getResources().getString(R.string.error_network));
            mLayoutWithoutNetwork.setVisibility(View.VISIBLE);
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        mTxtErrorFetchData.setVisibility(View.VISIBLE);
        mTxtSadFace.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showData() {
        mTxtErrorFetchData.setVisibility(View.INVISIBLE);
        mLayoutWithoutNetwork.setVisibility(View.INVISIBLE);
        mTxtSadFace.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    @Override
    public void processInitiate(int loaderId) {
        if (loaderId == MovieLoader.MOVIE_LOADER_ID) {
            mMoviesProgressBar.setVisibility(View.VISIBLE);
        } else {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                mMoviesAdapter.setMovies(null);
                loadMovies(mChoosedFilter);
                break;
            case R.id.action_filter_favorites:
                mChoosedFilter = "favorites";
                mMoviesAdapter.setMovies(null);
                loadMovies(FAVORITES);
                break;
            case R.id.action_filter_now_playing:
                mChoosedFilter = NOW_PLAYING;
                mMoviesAdapter.setMovies(null);
                loadMovies(NOW_PLAYING);
                break;
            case R.id.action_filter_popularity:
                mChoosedFilter = POPULAR;
                mMoviesAdapter.setMovies(null);
                loadMovies(POPULAR);
                break;
            case R.id.action_filter_rating:
                mChoosedFilter = TOP_RATED;
                mMoviesAdapter.setMovies(null);
                loadMovies(TOP_RATED);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CHOOSED_FILTER, mChoosedFilter);
        super.onSaveInstanceState(outState);
    }
}
