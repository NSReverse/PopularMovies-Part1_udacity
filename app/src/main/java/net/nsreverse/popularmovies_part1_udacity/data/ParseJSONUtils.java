package net.nsreverse.popularmovies_part1_udacity.data;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.nsreverse.popularmovies_part1_udacity.R;
import net.nsreverse.popularmovies_part1_udacity.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * This class parses the JSON as well as downloads the posters if they are available.
 *
 * Created by Robert on 5/26/2017.
 */

public class ParseJSONUtils {
    // Constants
    private static final String TAG = ParseJSONUtils.class.getSimpleName();

    /**
     * This method parses the JSON retrieved from MoviesAsyncTask as well as downloads
     * thumbnails.
     *
     * @param context Used to get the resource of a dummy image if load fails.
     * @param json The raw JSON as a String from MoviesAsyncTask.
     * @return A Movie array representing the JSON data source.
     * @throws JSONException An Exception raised if there was a problem parsing JSON.
     */
    public static Movie[] parseJSON(Context context, String json) throws JSONException {
        JSONObject baseObject = new JSONObject(json);
        JSONArray resultArray = baseObject.getJSONArray("results");

        Movie[] movies = new Movie[resultArray.length()];

        for (int i = 0; i < movies.length; i++) {
            JSONObject currentResult = resultArray.getJSONObject(i);

            Movie currentMovie = new Movie();
            currentMovie.setTitle(currentResult.getString("title"));
            try {
                currentMovie.setThumbnail(NetworkUtils.downloadThumbnailFromPath(
                        currentResult.getString("poster_path").replace("/", "")
                ));
            }
            catch (IOException ex) {
                // Catching here makes it to where it doesn't stop the entire load if one image fails.
                currentMovie.setThumbnail(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_image_black_24dp));
                Log.d(TAG, "Unable to retrieve image resource: " + ex.getMessage());
            }
            currentMovie.setPlotSynopsis(currentResult.getString("overview"));
            currentMovie.setReleaseDate(currentResult.getString("release_date"));
            currentMovie.setVoteAverage(currentResult.getString("vote_average"));
            currentMovie.setThumbnailAddress(
                    currentResult.getString("poster_path").replace("/", "")
            );

            movies[i] = currentMovie;
        }

        return movies;
    }
}
