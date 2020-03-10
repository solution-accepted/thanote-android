package edu.uci.thanote.apis.openmoviedb;

import com.google.gson.annotations.SerializedName;

public class OMDbMovie {
    @SerializedName("Response")
    private String response; // "True" or "False"
    @SerializedName("Error")
    private String error; // error message
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    @SerializedName("Rated")
    private String rated;
    @SerializedName("Released")
    private String released;
    @SerializedName("Runtime")
    private String runtime;
    @SerializedName("Genre")
    private String genre;
    @SerializedName("Director")
    private String director;
    @SerializedName("Writer")
    private String writer;
    @SerializedName("Actors")
    private String actors;
    @SerializedName("Plot")
    private String plot;
    @SerializedName("Language")
    private String language;
    @SerializedName("Country")
    private String country;
    @SerializedName("Awards")
    private String awards;
    @SerializedName("Poster")
    private String imageUrl;
    private String imdbRating;
    private String imdbVotes;
    @SerializedName("imdbID")
    private String imdbID;
    @SerializedName("Production")
    private String production;
    @SerializedName("Website")
    private String website;

    public String getTitle() {
        return title;
    }

    public String getResponse() {
        return response;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPlot() {
        return plot;
    }

    public String getImdbUrl() {
        return "https://www.imdb.com/title/" + imdbID;
    }

    @Override
    public String toString() {
        return "OMDbMovie{" +
                "response='" + response + '\'' +
                ", error='" + error + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", rated='" + rated + '\'' +
                ", released='" + released + '\'' +
                ", runtime='" + runtime + '\'' +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", writer='" + writer + '\'' +
                ", actors='" + actors + '\'' +
                ", plot='" + plot + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                ", awards='" + awards + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imdbRating='" + imdbRating + '\'' +
                ", imdbVotes='" + imdbVotes + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", production='" + production + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
