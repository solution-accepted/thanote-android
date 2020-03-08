package edu.uci.thanote.apis.omdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OMDbInterface {
    /**
     * @param title title of the movie
     * @return only one movie or error message. Check "Response" before use.
     */
    @GET("?")
    Call<OMDbMovie> getOMDbMovieByTitle(@Query("apikey") String apiKey,
                                        @Query("t") String title);

    /**
     * @param keyword Movie title to search for.
     * @param page    Page number to return.
     * @return a movie list or error message. Check "Response" before use.
     */
    @GET("?")
    Call<OMDbMovieSearchResponse> getOMDbMovieBySearching(@Query("apikey") String apiKey,
                                                          @Query("s") String keyword,
                                                          @Query("page") int page);

}
