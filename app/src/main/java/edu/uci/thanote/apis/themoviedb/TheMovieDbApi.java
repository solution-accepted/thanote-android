package edu.uci.thanote.apis.themoviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbApi {
    @GET("search/movie")
    Call<TopRatedResponse> queryMovie(@Query("api_key") String apiKey, @Query("query") String keyword);

    @GET("movie/top_rated")
    Call<TopRatedResponse> getTopRatedResponse(@Query("api_key") String apiKey);
}