package net.nsreverse.popularmovies_part1_udacity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.nsreverse.popularmovies_part1_udacity.data.NetworkUtils;
import net.nsreverse.popularmovies_part1_udacity.data.NetworkUtils.Sort;
import net.nsreverse.popularmovies_part1_udacity.data.ParseJSONUtils;
import net.nsreverse.popularmovies_part1_udacity.model.Movie;

import java.io.ByteArrayOutputStream;

/**
 * This Activity handles the initial display of movies depending on the sort criteria that the
 * user selects.
 */
public class MainActivity extends AppCompatActivity
                          implements MovieAdapter.MovieAdapterOnClickHandler {

    // Fields
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private Toast mToast;
    private Sort mCurrentSort;

    /**
     * This is the entry point for this Activity.
     *
     * @param savedInstanceState A Bundle indicating if restore information is available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar_loading);
        mErrorTextView = (TextView)findViewById(R.id.text_view_error);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_movies);
        Spinner mSortSpinner = (Spinner)findViewById(R.id.spinner_sort);

        String[] spinnerItems = {
                getString(R.string.spinner_popular),
                getString(R.string.spinner_top_rated)
        };
        mSortSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerItems));
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mCurrentSort = Sort.POPULAR;
                }
                else {
                    mCurrentSort = Sort.TOP_RATED;
                }

                reloadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ignore
            }
        });

        mMovieAdapter = new MovieAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, LinearLayoutManager.VERTICAL);
        layoutManager.setSpanCount(2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);

        mCurrentSort = Sort.POPULAR;
        showMovieRecyclerView(true);
    }

    /**
     * This method overrides the onResume method to reload the data source automatically when
     * coming back to MainActivity.
     */
    @Override
    protected void onResume() {
        super.onResume();

        reloadData();
    }

    /**
     * This method disables and enables either the RecyclerView or the error message TextView
     * depending on the boolean passed to it.
     *
     * @param visible A boolean representing if the RecyclerView should be hidden and the
     *                error TextView should be displayed.
     */
    private void showMovieRecyclerView(boolean visible) {
        mRecyclerView.setVisibility((visible)? View.VISIBLE : View.INVISIBLE);
        mErrorTextView.setVisibility((visible)? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * This method handles the click event of an item in the RecyclerView.
     *
     * @param selectedMovie A Movie object containing data on the selected movie.
     */
    @Override
    public void onMovieClick(Movie selectedMovie) {
        Log.d(TAG, getString(R.string.selected_movie) + selectedMovie.getTitle());

        // https://stackoverflow.com/questions/4352172/how-do-you-pass-images-bitmaps-between-android-activities-using-bundles/7890405#7890405
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        selectedMovie.getThumbnail().compress(Bitmap.CompressFormat.PNG, 50, outputStream);

        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.KEY_THUMBNAIL, outputStream.toByteArray());
        intent.putExtra(MovieDetailActivity.KEY_TITLE, selectedMovie.getTitle());
        intent.putExtra(MovieDetailActivity.KEY_PLOT_SYNOPSIS, selectedMovie.getPlotSynopsis());
        intent.putExtra(MovieDetailActivity.KEY_RELEASE_DATE, selectedMovie.getReleaseDate());
        intent.putExtra(MovieDetailActivity.KEY_VOTE_AVERAGE, selectedMovie.getVoteAverage());
        startActivity(intent);
    }

    /**
     * This is a convenience method to quickly reload the data source and display it.
     * It sets a new MovieAdapter to the RecyclerView to temporarily clear it while loading
     * data.
     */
    private void reloadData() {
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        new MoviesAsyncTask().execute(mCurrentSort);
    }

    /**
     * This class runs instructions in the background to download JSON and the respective poster
     * images of movies.
     */
    private class MoviesAsyncTask extends AsyncTask<Sort, Void, Movie[]> {

        /**
         * This method handles overriding onPreExecute to show the indeterminate ProgressBar while
         * loading data.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * This method runs in the background to retrieve desired data.
         *
         * @param params A Sort enum passed in to determine which api link to use. Only the first
         *               of the varargs is used.
         * @return An array of Movie objects.
         */
        @Override
        protected Movie[] doInBackground(Sort... params) {
            Movie[] movies;

            try {
                String json = NetworkUtils.getJSONFromURLWithSort(mCurrentSort);
                movies = ParseJSONUtils.parseJSON(MainActivity.this, json);
            }
            catch (Exception ex) {
                // General Exception as the basic handling is identical.
                Log.d(MoviesAsyncTask.class.getSimpleName(),
                        getString(R.string.error_load_data_detail) + ex.getMessage());

                movies = null;
            }

            return movies;
        }

        /**
         * This method handles UI updating of the new Movie array and whether or not the
         * RecyclerView should be hidden.
         *
         * @param movies A Movie array to update the RecyclerView's adapter.
         */
        @Override
        protected void onPostExecute(Movie[] movies) {
            if (movies != null && mMovieAdapter != null) {
                showMovieRecyclerView(true);
                mMovieAdapter.setMovieData(movies);
            }
            else {
                showMovieRecyclerView(false);

                if (mToast != null) {
                    mToast.cancel();
                }

                mToast = Toast.makeText(MainActivity.this,
                        getResources().getString(R.string.error_load_data), Toast.LENGTH_LONG);
                mToast.show();
            }

            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
