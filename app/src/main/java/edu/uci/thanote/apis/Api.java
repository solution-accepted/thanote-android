package edu.uci.thanote.apis;

public enum Api {
    JOKE("Joke"), RECIPEPUPPY("Recipe Puppy"), OMDB("OMDb"), THEMOVEDB("The Movie Db");
    private String apiName;
    private Api(String apiName) {
        this.apiName = apiName;
    }

    @Override
    public String toString(){
        return apiName;
    }

    public static Api toApi(String apiName) {
        if (apiName.equals(Api.JOKE.toString())) {
            return JOKE;
        } else if (apiName.equals(Api.RECIPEPUPPY.toString())) {
            return RECIPEPUPPY;
        } else if (apiName.equals(Api.OMDB.toString())) {
            return OMDB;
        } else if (apiName.equals(Api.THEMOVEDB.toString())) {
            return THEMOVEDB;
        }

        return JOKE;
    }

    public static String[] getAllApiNames() {
        return new String[] {
            Api.JOKE.toString(), Api.RECIPEPUPPY.toString(), Api.OMDB.toString(), Api.THEMOVEDB.toString()
        };
    }

    public String getApiKey() {
        switch (this) {
            case OMDB:
                return "7c782685";
            case THEMOVEDB:
                return "698b1027c17fe80eecb1d699d1806254";
            default:
                return "";
        }
    }
}
