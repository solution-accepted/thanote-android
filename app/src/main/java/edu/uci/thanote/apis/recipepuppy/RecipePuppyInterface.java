package edu.uci.thanote.apis.recipepuppy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipePuppyInterface {
    /**
     * @param ingredients comma delimited ingredients
     * @param query normal search query
     * @param page page
     */
    @GET("?")
    Call<RecipePuppyResponse> getRecipePuppyResponse(@Query("i") String ingredients, @Query("q") String query, @Query("p") int page);
}
