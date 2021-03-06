package edu.uci.thanote.apis.joke;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JokeApi {
    @GET("Any?type=single&blacklistFlags=nsfw,religious,political,racist,sexist")
    Call<SingleJoke> getSingleJoke();

    @GET("Any?type=single")
    Call<SingleJoke> getSingleJokeBy(@Query("contains") String key, @Query("blacklistFlags") String flags);

    @GET("Any?type=single&blacklistFlags=nsfw,religious,political,racist,sexist")
    Call<SingleJoke> getSingleJokeBy(@Query("contains") String key);

    @GET("Any?type=twopart&blacklistFlags=nsfw,religious,political,racist,sexist")
    Call<TwoPartJoke> getTwoPartJoke();

    @GET("Any?type=twopart")
    Call<TwoPartJoke> getTwoPartJokeBy(@Query("contains") String key, @Query("blacklistFlags") String flags);

    @GET("Any?type=twopart&blacklistFlags=nsfw,religious,political,racist,sexist")
    Call<TwoPartJoke> getTwoPartJokeBy(@Query("contains") String key);
}
