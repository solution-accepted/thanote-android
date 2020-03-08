package edu.uci.thanote.apis.omdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OMDbApi {
    /**
     * @param title title of the movie
     */
    @GET("?")
    Call<OMDb> getOMDb(@Query("t") String title, @Query("apikey") String apiKey);
}
