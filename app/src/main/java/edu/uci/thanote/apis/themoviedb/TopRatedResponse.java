package edu.uci.thanote.apis.themoviedb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopRatedResponse {
    private int page;

    @SerializedName("total_results")
    private int totalResult;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
