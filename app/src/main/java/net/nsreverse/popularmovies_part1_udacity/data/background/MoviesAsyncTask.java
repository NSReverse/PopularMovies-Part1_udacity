package net.nsreverse.popularmovies_part1_udacity.data.background;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.nsreverse.popularmovies_part1_udacity.R;
import net.nsreverse.popularmovies_part1_udacity.data.NetworkUtils;
import net.nsreverse.popularmovies_part1_udacity.data.ParseJSONUtils;
import net.nsreverse.popularmovies_part1_udacity.model.Movie;

/**
 * This class runs instructions in the background to download JSON and the respective poster
 * images of movies.
 */
public class MoviesAsyncTask extends AsyncTask<NetworkUtils.Sort, Void, Movie[]> {

    private NetworkUtils.Sort mCurrentSort;
    private Context context;
    private Delegate delegate;

    public interface Delegate {
        void moviesDownloaded(Movie[] movies);
    }

    public MoviesAsyncTask(Context context, NetworkUtils.Sort currentSort) {
        mCurrentSort = currentSort;
        this.context = context;

        if (context instanceof Delegate) {
            delegate = (Delegate)context;
        }
    }

    /**
     * This method handles overriding onPreExecute to show the indeterminate ProgressBar while
     * loading data.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * This method runs in the background to retrieve desired data.
     *
     * @param params A Sort enum passed in to determine which api link to use. Only the first
     *               of the varargs is used.
     * @return An array of Movie objects.
     */
    @Override
    protected Movie[] doInBackground(NetworkUtils.Sort... params) {
        Movie[] movies;

        try {
            String json = NetworkUtils.getJSONFromURLWithSort(mCurrentSort);
            movies = ParseJSONUtils.parseJSON(context, json);
        }
        catch (Exception ex) {
            // General Exception as the basic handling is identical.
            Log.d(MoviesAsyncTask.class.getSimpleName(),
                    context.getString(R.string.error_load_data_detail) + ex.getMessage());

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
        if (delegate != null) delegate.moviesDownloaded(movies);
    }
}