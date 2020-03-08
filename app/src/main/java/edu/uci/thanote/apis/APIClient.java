package edu.uci.thanote.apis;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String DEMO_BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String JOKE_BASE_URL = "https://sv443.net/jokeapi/v2/joke/";
    private static final String RECIPE_PUPPY_BASE_URL = "http://www.recipepuppy.com/api/";
    private static final String OMDB_BASE_URL = "http://omdbapi.com/";

    private static APIClient instance;
    private Retrofit retrofitDemo;
    private Retrofit retrofitJoke;
    private Retrofit retrofitRecipePuppy;
    private Retrofit retrofitOMDb;

    public static APIClient getInstance() {
        if (instance == null) {
            synchronized(APIClient.class) {
                instance = new APIClient();
            }
        }
        return instance;
    }

    public Retrofit getRetrofitDemo() {
        if (retrofitDemo == null) {
            retrofitDemo = buildRetrofit(DEMO_BASE_URL);
        }

        return retrofitDemo;
    }

    public Retrofit getRetrofitJoke() {
        if (retrofitJoke == null) {
            retrofitJoke = buildRetrofit(JOKE_BASE_URL);
        }

        return  retrofitJoke;
    }

    public Retrofit getRetrofitRecipePuppy() {
        if (retrofitRecipePuppy == null) {
            retrofitRecipePuppy = buildRetrofit(RECIPE_PUPPY_BASE_URL);
        }

        return  retrofitRecipePuppy;
    }

    public Retrofit getRetrofitOMDb() {
        if (retrofitOMDb == null) {
            retrofitOMDb = buildRetrofit(OMDB_BASE_URL);
        }

        return retrofitOMDb;
    }

    private Retrofit buildRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
