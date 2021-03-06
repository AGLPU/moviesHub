package aman.bramdehart.moviesL.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Acer on 9/11/2016.
 */
public class Movie implements Serializable{

    @SerializedName("original_title")
    private
    String title;

    @SerializedName("release_date")
    private
    String date;

    @SerializedName("vote_average")
    private
    double rating;

    @SerializedName("poster_path")
    private
    String posterPath;

    @SerializedName("id")
    private
    int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
