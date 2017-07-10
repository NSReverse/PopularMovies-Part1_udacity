package net.nsreverse.popularmovies_part1_udacity.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Class that downloads JSON data from an API at themovieDB. Derived from Udacity's NetworkUtils.
 *
 * Created by Robert on 5/26/2017.
 */

public final class NetworkUtils {

    private NetworkUtils() { }

    public enum Sort {
        POPULAR,
        TOP_RATED
    }

    // Constants
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_THUMBNAIL_185_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = ""; // Place your API key here.
    private static final String[] PATHS = { "popular", "top_rated" };

    /**
     * Downloads JSON data downloaded from themovieDB and returns it.
     *
     * @param currentSort A Sort enum parameter to determine which API path to use.
     * @return A String representing the downloaded JSON data.
     * @throws IOException An Exception thrown when there is a network error.
     */
    public static String getJSONFromURLWithSort(Sort currentSort) throws IOException {
        Uri uri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath((currentSort == Sort.POPULAR)? PATHS[0] : PATHS[1])
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        Log.d(TAG, "URI: " + uri.toString());

        URL url = new URL(uri.toString());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        try {
            InputStream is = conn.getInputStream();

            Scanner scn = new Scanner(is);
            scn.useDelimiter("\\A");

            if (scn.hasNext()) {
                return scn.next();
            }
            else {
                return null;
            }
        }
        finally {
            conn.disconnect();
        }
    }

    /**
     * Downloads data from a URL and decodes it into a Bitmap.
     *
     * @param thumbnailPath A path component locating the remote image resource.
     * @return A Bitmap representing the thumbnail downloaded.
     * @throws IOException An Exception raised if there was an issue downloading.
     */
    public static Bitmap downloadThumbnailFromPath(String thumbnailPath) throws IOException {
        Uri uri = Uri.parse(API_THUMBNAIL_185_BASE_URL).buildUpon()
                .appendPath(thumbnailPath)
                .build();

        URL url = new URL(uri.toString());

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        try {
            InputStream is = conn.getInputStream();

            // https://developer.android.com/reference/android/graphics/BitmapFactory.html
            return BitmapFactory.decodeStream(is);
        }
        finally {
            conn.disconnect();
        }
    }
}
