package net.nsreverse.popularmovies_part1_udacity.model;

import android.graphics.Bitmap;

/**
 * Basic model for holding a single piece of a data source of movies.
 *
 * Created by Robert on 5/26/2017.
 */
public class Movie {
    private String title;
    private Bitmap thumbnail;
    private String thumbnailAddress;
    private String releaseDate;
    private String voteAverage;
    private String plotSynopsis;

    public Movie() {
        title = "";
        thumbnailAddress = "";
        releaseDate = "";
        voteAverage = "";
        plotSynopsis = "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setThumbnailAddress(String thumbnailAddress) {
        this.thumbnailAddress = thumbnailAddress;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public String getThumbnailAddress() {
        return thumbnailAddress;
    }
}
