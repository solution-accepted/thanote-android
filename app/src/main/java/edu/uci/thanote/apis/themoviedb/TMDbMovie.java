package edu.uci.thanote.apis.themoviedb;

import com.google.gson.annotations.SerializedName;
import edu.uci.thanote.apis.ImageNote;

public class TMDbMovie implements ImageNote {
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;

    @Override
    public String getTitle() {
        return "Movie: " + title;
    }

    @Override
    public String getDetail() {
        return overview;
    }

    public String getOverview() {
        return overview;
    }

    public String getImageUrl() {
        return posterPath == null || posterPath.equals("null") ?
                "" :
                "https://image.tmdb.org/t/p/w500/" + posterPath;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
