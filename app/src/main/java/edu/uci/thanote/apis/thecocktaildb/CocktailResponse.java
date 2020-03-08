package edu.uci.thanote.apis.thecocktaildb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CocktailResponse {
    @SerializedName("drinks")
    private List<Cocktail> cocktails;

    public List<Cocktail> getCocktails() {
        return cocktails;
    }
}
