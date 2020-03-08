package edu.uci.thanote.apis.omdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OMDbMovieSearchResponse {
    @SerializedName("Response")
    private String response; // "True" or "False"
    @SerializedName("Error")
    private String error; // error message
    @SerializedName("Search")
    private List<Movie> results;
    @SerializedName("totalResults")
    private String totalResults;

    public static class Movie {
        @SerializedName("Title")
        private String title;
        @SerializedName("Year")
        private String year;
        @SerializedName("imdbID")
        private String imdbId;
        @SerializedName("Type")
        private String type;
        @SerializedName("Poster")
        private String imageUrl;

        public String getTitle() {
            return title;
        }

        public String getImdbId() {
            return imdbId;
        }

        public String getImdbUrl() {
            return "https://www.imdb.com/title/" + imdbId;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }

    public List<Movie> getResults() {
        return results;
    }
}
