package edu.uci.thanote.apis;

import java.util.Arrays;

public enum Api {
    JOKE("Joke"),
    RECIPEPUPPY("Recipe Puppy"),
    OMDB("OMDb"),
    THEMOVIEDB("The Movie Db"),
    THECOCKTAILDB("The Cocktail Db");

    private String apiName;

    private Api(String apiName) {
        this.apiName = apiName;
    }

    @Override
    public String toString() {
        return apiName;
    }

    public static Api toApi(String apiName) {
        if (apiName.equals(Api.JOKE.toString())) {
            return JOKE;
        } else if (apiName.equals(Api.RECIPEPUPPY.toString())) {
            return RECIPEPUPPY;
        } else if (apiName.equals(Api.OMDB.toString())) {
            return OMDB;
        } else if (apiName.equals(Api.THEMOVIEDB.toString())) {
            return THEMOVIEDB;
        } else if (apiName.equals(Api.THECOCKTAILDB.toString())) {
            return THECOCKTAILDB;
        }

        return JOKE;
    }

    public static String[] getAllApiNames() {
        return Arrays.stream(values()).map(Enum::toString).toArray(String[]::new);
    }

    public String getApiKey() {
        switch (this) {
            case OMDB:
                return "7c782685";
            case THEMOVIEDB:
                return "698b1027c17fe80eecb1d699d1806254";
            default:
                return "";
        }
    }
}
