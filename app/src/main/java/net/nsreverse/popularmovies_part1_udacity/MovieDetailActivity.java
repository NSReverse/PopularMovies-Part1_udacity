package net.nsreverse.popularmovies_part1_udacity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This Activity handles displaying of detailed information about a movie that is clicked
 * in MainActivity.
 */
public class MovieDetailActivity extends AppCompatActivity {

    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_THUMBNAIL = "THUMBNAIL";
    public static final String KEY_RELEASE_DATE = "RELEASE_DATE";
    public static final String KEY_VOTE_AVERAGE = "VOTE_AVERAGE";
    public static final String KEY_PLOT_SYNOPSIS = "SYNOPSIS";

    /**
     * This is the entry point for this Activity.
     *
     * @param savedInstanceState A Bundle indicating if restore information is available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setTitle(R.string.activity_movie_detail);

        // https://developer.android.com/training/implementing-navigation/ancestral.html
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView thumbImageView = (ImageView)findViewById(R.id.image_view_thumbnail);
        TextView titleTextView = (TextView)findViewById(R.id.text_view_title);
        TextView releaseDateTextView = (TextView)findViewById(R.id.text_view_release_date);
        TextView averageRatingTextView = (TextView)findViewById(R.id.text_view_rating);
        TextView synopsisTextView = (TextView)findViewById(R.id.text_view_synopsis);

        // https://stackoverflow.com/questions/4352172/how-do-you-pass-images-bitmaps-between-android-activities-using-bundles/7890405#7890405
        byte[] thumbnailArray = getIntent().getByteArrayExtra(KEY_THUMBNAIL);
        Bitmap thumbnail = BitmapFactory.decodeByteArray(thumbnailArray, 0, thumbnailArray.length);

        String title = getIntent().getStringExtra(KEY_TITLE);
        String releaseDate = getIntent().getStringExtra(KEY_RELEASE_DATE);
        String averageRating = getIntent().getStringExtra(KEY_VOTE_AVERAGE);
        String plotSynopsis = getIntent().getStringExtra(KEY_PLOT_SYNOPSIS);

        thumbImageView.setImageBitmap(thumbnail);
        titleTextView.setText(title);
        releaseDateTextView.setText(getString(R.string.released) + " " + releaseDate);
        averageRatingTextView.setText(getString(R.string.rating) + " " + averageRating);
        synopsisTextView.setText(plotSynopsis);
    }

    /**
     * This method handles button click events in the ActionBar. Currently it only handles the
     * up navigation back button.
     *
     * @param item The MenuItem that was clicked.
     * @return A boolean representing if the action was handled.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // https://developer.android.com/training/implementing-navigation/ancestral.html
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
