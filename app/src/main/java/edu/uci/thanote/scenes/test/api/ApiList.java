package edu.uci.thanote.scenes.test.api;

public enum ApiList {
    JOKE("Joke", 0), RECIPEPUPPY("Recipe Puppy", 1), OMDB("OMDb", 2);
    private String apiName;
    private int tag;
    private ApiList(String apiName, int tag) {
        this.apiName = apiName;
        this.tag = tag;
    }

    @Override
    public String toString(){
        return apiName;
    }

    public int getTag() {
        return tag;
    }

    public static ApiList toApi(String apiName) {
        if (apiName.equals(ApiList.JOKE.toString())) {
            return JOKE;
        } else if (apiName.equals(ApiList.RECIPEPUPPY.toString())) {
            return RECIPEPUPPY;
        } else if (apiName.equals(ApiList.OMDB.toString())) {
            return OMDB;
        }

        return JOKE;
    }

    public static String[] getAllApiNames() {
        return new String[] {
            ApiList.JOKE.toString(), ApiList.RECIPEPUPPY.toString(), ApiList.OMDB.toString()
        };
    }
}
