package edu.uci.thanote.apis.recipepuppy;

import com.google.gson.annotations.SerializedName;

public class Recipe {
    private String title;
    @SerializedName("href")
    private String websiteUrl;
    private String thumbnail;
    private String ingredients;

    public String getTitle() {
        return title;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", ingredients='" + ingredients + '\'' +
                '}';
    }
}
