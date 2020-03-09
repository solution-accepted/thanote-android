package edu.uci.thanote.apis.themoviedb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMDbMoviesResponse {
    private int page;

    @SerializedName("total_results")
    private int totalResult;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<TMDbMovie> movies;

    public List<TMDbMovie> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return "MoviesResponse{" +
                "page=" + page +
                ", totalResult=" + totalResult +
                ", totalPages=" + totalPages +
                ", movies=" + movies +
                '}';
    }
}
