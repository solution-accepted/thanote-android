package edu.uci.thanote.apis;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static APIClient instance;
    private Retrofit retrofitDemo;
    private Retrofit retrofitJoke;
    private static final String DEMO_BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String JOKE_BASE_URL = "https://sv443.net/jokeapi/v2/joke/";

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
            retrofitDemo = new Retrofit.Builder()
                    .baseUrl(DEMO_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitDemo;
    }

    public Retrofit getRetrofitJoke() {
        if (retrofitJoke == null) {
            retrofitJoke = new Retrofit.Builder()
                    .baseUrl(JOKE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return  retrofitJoke;
    }
}
