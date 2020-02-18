package edu.uci.thanote.scenes.test.api;

import android.widget.TextView;
import android.os.Bundle;
import edu.uci.thanote.R;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.joke.JokeAPIInterface;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.recipepuppy.Recipe;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyInterface;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.scenes.test.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

public class ApiResultTestActivity extends BaseActivity {
    public static final String EXTRA_APINAME = "edu.uci.thanote.test.api.EXTRA_APINAME";
    private TextView resultTextView;
    private String log = "";
    private Retrofit retrofitJoke = APIClient.getInstance().getRetrofitJoke();
    private Retrofit retrofitRecipePuppy = APIClient.getInstance().getRetrofitRecipePuppy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_result_test);
        setupViews();
        testApis();
    }

    private void setupViews() {
        resultTextView = findViewById(R.id.text_view_api_result_test);
    }

    private void testApis() {
        String apiName = getIntent().getStringExtra(EXTRA_APINAME);
        if (apiName != null) {
            ApiList api = ApiList.toApi(apiName);
            switch (api) {
                case JOKE:
                    testJoke();
                    break;
                case RECIPEPUPPY:
                    testRecipePuppy();
                    break;
            }
        }
    }

    private void testJoke() {
        fetchSingleJoke();
        fetchTwoPartJoke();
        fetchSingleJokeBy("why");
        fetchTwoPartJokeBy("why");
    }

    private void fetchSingleJoke() {
        JokeAPIInterface api = retrofitJoke.create(JokeAPIInterface.class);
        Call<SingleJoke> call = api.getSingleJoke();
        singleJokeCall(call);
    }

    private void fetchTwoPartJoke() {
        JokeAPIInterface api = retrofitJoke.create(JokeAPIInterface.class);
        Call<TwoPartJoke> call = api.getTwoPartJoke();
        twoPartJokeCall(call);
    }

    private void twoPartJokeCall(Call<TwoPartJoke> call) {
        call.enqueue(new Callback<TwoPartJoke>() {
            @Override
            public void onResponse(Call<TwoPartJoke> call, Response<TwoPartJoke> response) {
                if (!response.isSuccessful()) {
                    log += "Response Code: " + response.code();
                    resultTextView.setText(log);
                    return;
                }

                TwoPartJoke joke = response.body();

                if (joke != null) {
                    log += joke.toString();
                } else {
                    log += "Joke is null!";
                }
                resultTextView.setText(log);
            }

            @Override
            public void onFailure(Call<TwoPartJoke> call, Throwable t) {
                log += "Error: " + t.getMessage();
                resultTextView.setText(log);
            }
        });
    }

    private void fetchSingleJokeBy(String key) {
        JokeAPIInterface api = retrofitJoke.create(JokeAPIInterface.class);
        Call<SingleJoke> call = api.getSingleJokeBy(key);
        singleJokeCall(call);
    }

    private void singleJokeCall(Call<SingleJoke> call) {
        call.enqueue(new Callback<SingleJoke>() {
            @Override
            public void onResponse(Call<SingleJoke> call, Response<SingleJoke> response) {
                if (!response.isSuccessful()) {
                    log += "Response Code: " + response.code();
                    resultTextView.setText(log);
                    return;
                }

                SingleJoke joke = response.body();

                if (joke != null) {
                    log += joke.toString();
                } else {
                    log += "Joke is null!";
                }
                resultTextView.setText(log);
            }

            @Override
            public void onFailure(Call<SingleJoke> call, Throwable t) {
                log += "Error: " + t.getMessage();
                resultTextView.setText(log);
            }
        });
    }

    private void fetchTwoPartJokeBy(String key) {
        JokeAPIInterface api = retrofitJoke.create(JokeAPIInterface.class);
        Call<TwoPartJoke> call = api.getTwoPartJokeBy(key);
        twoPartJokeCall(call);
    }

    private void testRecipePuppy() {
        RecipePuppyInterface api = retrofitRecipePuppy.create(RecipePuppyInterface.class);
        Call<RecipePuppyResponse> call = api.getRecipePuppyResponse("", "", 1);
        call.enqueue(new Callback<RecipePuppyResponse>() {
            @Override
            public void onResponse(Call<RecipePuppyResponse> call, Response<RecipePuppyResponse> response) {
                if (!response.isSuccessful()) {
                    log += "Response Code: " + response.code();
                    resultTextView.setText(log);
                    return;
                }

                List<Recipe> recipes = response.body().getRecipes();

                if (recipes != null) {
                    StringBuilder builder = new StringBuilder();

                    for (Recipe recipe : recipes) {
                        builder.append(recipe.toString() + "\n\n");
                    }
                    resultTextView.setText(builder.toString());
                } else {
                    resultTextView.setText("Recipes are null!");
                }
            }

            @Override
            public void onFailure(Call<RecipePuppyResponse> call, Throwable t) {
                log += "Error: " + t.getMessage();
                resultTextView.setText(log);
            }
        });
    }
}
