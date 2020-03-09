package edu.uci.thanote.apis.recipepuppy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Random;

public interface RecipePuppyApi {

    static int getRandomPageNumber() {
        final int PAGE_MIN = 1;
        final int PAGE_MAX = 100;
        return PAGE_MIN + new Random().nextInt(PAGE_MAX);
    }

    /**
     * @param ingredients comma delimited ingredients
     * @param query       normal search query
     * @param page        page number, range [1, 100]
     */
    @GET("?")
    Call<RecipePuppyResponse> getRecipePuppyResponse(@Query("i") String ingredients, @Query("q") String query, @Query("p") int page);
}
