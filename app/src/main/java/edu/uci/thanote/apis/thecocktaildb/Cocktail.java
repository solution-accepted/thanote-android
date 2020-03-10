package edu.uci.thanote.apis.thecocktaildb;

import com.google.gson.annotations.SerializedName;
import edu.uci.thanote.apis.ImageNote;

public class Cocktail implements ImageNote {
    @SerializedName("strDrink")
    private String name;
    @SerializedName("strInstructions")
    private String instruction;
    @SerializedName("strDrinkThumb")
    private String imageUrl;

    public String getName() {
        return name;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "Cocktail{" +
                "name='" + name + '\'' +
                ", instruction='" + instruction + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public String getTitle() {
        return "Cocktail: " + name;
    }

    @Override
    public String getDetail() {
        return instruction;
    }
}