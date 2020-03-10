package edu.uci.thanote.apis.opentriviadb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joxon on 2020-03-09.
 */
public interface TriviaApi {
    @GET("api.php?amount=1&type=multiple")
    Call<TriviaResponse> getSingleMultipleChoiceTrivia();

    @GET("api.php?type=multiple")
    Call<TriviaResponse> getMultipleChoiceTrivia(@Query("amount") int amount);

    @GET("api.php?amount=1&type=boolean")
    Call<TriviaResponse> getSingleTrueFalseTrivia();
}
