package edu.uci.thanote.apis.joke;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JokeAPIInterface {
    @GET("Any?type=twopart")
    Call<TwoPartJoke> getTwoPartJoke();

    @GET("Any?type=single")
    Call<SingleJoke> getSingleJoke();

    @GET("Any?")
    Call<SingleJoke> getSingleJokeBy(@Query("type=single&contains") String key);

    @GET("Any?")
    Call<TwoPartJoke> getTwoPartJokeBy(@Query("type=twopart&contains") String key);
}
