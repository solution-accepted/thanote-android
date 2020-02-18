package edu.uci.thanote.apis.recipepuppy;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipePuppyResponse {
    private String title;
    private double version;
    @SerializedName("href")
    private String url;
    @SerializedName("results")
    private List<Recipe> recipes;

    public String getTitle() {
        return title;
    }

    public double getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
