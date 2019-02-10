package com.example.android.mymovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymovies.api.NetworkUtils;
import com.example.android.mymovies.database.AppDatabase;
import com.example.android.mymovies.model.Movie;
import com.example.android.mymovies.model.Review;
import com.example.android.mymovies.model.Trailer;
import com.example.android.mymovies.services.loaders.ReviewLoader;
import com.example.android.mymovies.services.loaders.TrailerLoader;
import com.example.android.mymovies.utils.AppExecutors;
import com.example.android.mymovies.utils.LoaderDelegate;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderDelegate, TrailerAdapter.OnTrailerItemClick {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoaderCallback;
    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoaderCallback;

    private RecyclerView mReviewRecyclerView, mTrailerRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private ProgressBar mTrailersProgressBar, mReviewsProgressBar;

    private ImageView mImgMovie;
    private TextView mTxtTitle, mTxtReleaseDate, mTxtVoteAverage, mTxtOverview;
    private TextView mTrailerTxtTryAgain, mReviewTxtTryAgain;

    private LinearLayout mTrailerLayoutWithoutNetwork, mReviewLayoutWithoutNetwork;

    private static final String MOVIE_ID = "id";

    private static Movie movie = null;

    private AppDatabase mDb;

    private static boolean isFavoriteMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mReviewRecyclerView = findViewById(R.id.recyclerview_reviews);
        mTrailerRecyclerView = findViewById(R.id.recyclerview_trailers);

        LinearLayoutManager layoutManagerVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(layoutManagerVertical);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(layoutManagerHorizontal);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mTrailersProgressBar = findViewById(R.id.pb_list_trailers);
        mReviewsProgressBar = findViewById(R.id.pb_list_reviews);
        mImgMovie = findViewById(R.id.img_movie_detail);
        mTxtTitle = findViewById(R.id.txt_title);
        mTxtReleaseDate = findViewById(R.id.txt_release_date);
        mTxtVoteAverage = findViewById(R.id.txt_vote_average);
        mTxtOverview = findViewById(R.id.txt_overview);
        mTrailerTxtTryAgain = findViewById(R.id.trailer_txt_try_again);
        mReviewTxtTryAgain = findViewById(R.id.review_txt_try_again);

        mTrailerLayoutWithoutNetwork = findViewById(R.id.trailer_layout_without_network);
        mReviewLayoutWithoutNetwork = findViewById(R.id.review_layout_without_network);

        mTrailerTxtTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTrailers();
                loadReviews();
            }
        });

        mReviewTxtTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadReviews();
                loadTrailers();
            }
        });

        mDb = AppDatabase.getInstance(getApplicationContext());

        Bundle data = getIntent().getExtras();

        if (data != null) {
            movie = data.getParcelable("movie");
        }

        isFavorite();

        setupLoaders();

        setMovieDetails(movie);
        loadTrailers();
        loadReviews();
    }

    private void isFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Movie favoriteMovie = mDb.favoritesMoviesDAO().loadFavoriteMovieById(movie.getIdFromApi());
                if (favoriteMovie != null) {
                    isFavoriteMovie = true;
                } else {
                    isFavoriteMovie = false;
                }
            }
        });
    }

    private void setMovieDetails(Movie movie) {
        Picasso.get().load(NetworkUtils.getPosterUrl(movie.getPosterPath())).into(mImgMovie);
        mTxtTitle.setText(movie.getTitle());
        mTxtReleaseDate.setText(movie.getReleaseDate());
        mTxtVoteAverage.setText(movie.getVoteAverage().toString());
        mTxtOverview.setText(movie.getOverview());
    }

    private void loadTrailers() {
        if (NetworkUtils.isNetworkConnected(this)) {
            showTrailers();
            Bundle queryBundle = new Bundle();
            queryBundle.putString(MOVIE_ID, String.valueOf((movie.getIdFromApi())));
            getSupportLoaderManager().initLoader(TrailerLoader.TRAILER_LOADER_ID, queryBundle, trailerLoaderCallback);
        } else {
            mTrailerLayoutWithoutNetwork.setVisibility(View.VISIBLE);
            mTrailerRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void loadReviews() {
        if (NetworkUtils.isNetworkConnected(this)) {
            showReviews();
            Bundle queryBundle = new Bundle();
            queryBundle.putString(MOVIE_ID, String.valueOf((movie.getIdFromApi())));
            getSupportLoaderManager().initLoader(ReviewLoader.REVIEW_LOADER_ID, queryBundle, reviewLoaderCallback);
        } else {
            mReviewLayoutWithoutNetwork.setVisibility(View.VISIBLE);
            mReviewRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void showTrailers() {
        mTrailerLayoutWithoutNetwork.setVisibility(View.INVISIBLE);
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showReviews() {
        mReviewLayoutWithoutNetwork.setVisibility(View.INVISIBLE);
        mReviewRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setupLoaders() {
        trailerLoaderCallback = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
            @Override
            public Loader<List<Trailer>> onCreateLoader(int id, @Nullable Bundle args) {
                if (args == null) return null;
                String movieID = args.getString(TrailerLoader.MOVIE_ID);
                return new TrailerLoader(DetailActivity.this, DetailActivity.this, Integer.parseInt(movieID));
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Trailer>> loader, List<Trailer> data) {
                if (data != null && !data.equals("")) {
                    showTrailers();
                    mTrailersProgressBar.setVisibility(View.INVISIBLE);
                    mTrailerAdapter.setTrailers(data);
                } else {
                    Toast.makeText(DetailActivity.this, "Não foi possível carregar os trailers", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Trailer>> loader) {
            }
        };

        reviewLoaderCallback = new LoaderManager.LoaderCallbacks<List<Review>>() {
            @NonNull
            @Override
            public Loader<List<Review>> onCreateLoader(int id, @Nullable Bundle args) {
                if (args == null) return null;
                String movieID = args.getString(ReviewLoader.MOVIE_ID);
                return new ReviewLoader(DetailActivity.this, DetailActivity.this, Integer.parseInt(movieID));
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> data) {
                if (data != null && !data.equals("")) {
                    showReviews();
                    mReviewsProgressBar.setVisibility(View.INVISIBLE);
                    mReviewAdapter.setReviews(data);
                } else {
                    Toast.makeText(DetailActivity.this, "Não foi possível carregar os comentários", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Review>> loader) {
            }
        };
    }


    @Override
    public void processInitiate(int loaderId) {
        if (loaderId == ReviewLoader.REVIEW_LOADER_ID) {
            mReviewsProgressBar.setVisibility(View.VISIBLE);
        } else if (loaderId == TrailerLoader.TRAILER_LOADER_ID) {
            mTrailersProgressBar.setVisibility(View.VISIBLE);
        } else {
            return;
        }
    }

    @Override
    public void onTrailerItemClick(Trailer trailer) {
        try {
            String key = trailer.getKey();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkUtils.buildYoutubeUrl(key))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoritesMoviesDAO().insertFavoriteMovie(movie);
            }
        });
    }

    private void removeFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoritesMoviesDAO().deleteFavoriteMovie(movie);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);

        if (isFavoriteMovie) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String movieTitle = movie.getTitle();
        String movieOverview = movie.getOverview();

        switch (id) {
            case R.id.action_share:
                ShareCompat.IntentBuilder
                        .from(DetailActivity.this)
                        .setType("text/plain")
                        .setChooserTitle(R.string.share_sinopse_title)
                        .setText(R.string.sinopse + movieTitle + "\n\n" + movieOverview)
                        .startChooser();
                break;
            case R.id.action_favorite:
                if (movie.isFavorite()) {
                    removeFavorite();
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border));
                } else {
                    onFavorite();
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite));
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
