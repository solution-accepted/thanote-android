package edu.uci.thanote.apis.thecocktaildb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheCocktailDbApi {

    @GET("search.php")
    Call<CocktailResponse> getCocktailBySearching(@Query("s") String keyword);

    @GET("random.php")
    Call<CocktailResponse> getRandomCocktail();
}
