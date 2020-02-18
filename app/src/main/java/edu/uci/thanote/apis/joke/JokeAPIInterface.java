package edu.uci.thanote.apis.joke;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JokeAPIInterface {
    @GET("Any?type=twopart")
    Call<TwoPartJoke> getTwoPartJoke();

    @GET("Any?type=single")
    Call<SingleJoke> getSingleJoke();

    @GET("Any?type=single")
    Call<SingleJoke> getSingleJokeBy(@Query("contains") String key);

    @GET("Any?type=twopart")
    Call<TwoPartJoke> getTwoPartJokeBy(@Query("contains") String key);
}
