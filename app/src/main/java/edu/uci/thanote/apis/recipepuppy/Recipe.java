package edu.uci.thanote.apis.recipepuppy;

import com.google.gson.annotations.SerializedName;
import edu.uci.thanote.apis.ImageNote;

public class Recipe implements ImageNote {
    private String title;
    @SerializedName("href")
    private String websiteUrl;
    private String thumbnail;
    private String ingredients;

    @Override
    public String getTitle() {
        return "Recipe: " + title;
    }

    @Override
    public String getDetail() {
        return ingredients + "\n" + websiteUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    @Override
    public String getImageUrl() {
        return thumbnail;
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
