package edu.uci.thanote.scenes.test.api;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import edu.uci.thanote.R;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.joke.JokeAPIInterface;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.omdb.OMDb;
import edu.uci.thanote.apis.omdb.OMDbInterface;
import edu.uci.thanote.apis.recipepuppy.Recipe;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyInterface;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.scenes.test.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Random;

public class ApiResultTestActivity extends BaseActivity {
    public static final String EXTRA_APINAME = "edu.uci.thanote.test.api.EXTRA_APINAME";
    private String log = "";
    private String apiName;

    private static final String OMDB_APIKEY = "7c782685";

    // components
    private EditText queryEditText;
    private TextView resultTextView;
    private TextView titleTextView;

    // retrofits
    private Retrofit retrofitJoke = APIClient.getInstance().getRetrofitJoke();
    private Retrofit retrofitRecipePuppy = APIClient.getInstance().getRetrofitRecipePuppy();
    private Retrofit retrofitOMDb = APIClient.getInstance().getRetrofitOMDb();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_result_test);
        setupViews();
        apiName = getIntent().getStringExtra(EXTRA_APINAME);
        titleTextView.setText(apiName);
    }

    private void setupViews() {
        // components
        Button getButton = findViewById(R.id.button_api_test_get);
        getButton.setOnClickListener(buttonClickListener);
        Button queryButton = findViewById(R.id.button_api_test_query);
        queryButton.setOnClickListener(buttonClickListener);
        Button clearButton = findViewById(R.id.button_api_test_clear);
        clearButton.setOnClickListener(buttonClickListener);
        queryEditText = findViewById(R.id.edit_text_api_test_query);
        resultTextView = findViewById(R.id.text_view_api_result_test);
        titleTextView = findViewById(R.id.text_view_api_test_title);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_api_test_get:
                    clear();
                    testGetApis();
                    break;
                case R.id.button_api_test_query:
                    clear();
                    String keyword = queryEditText.getText().toString();
                    testQueryApis(keyword);
                    break;
                case R.id.button_api_test_clear:
                    clear();
                    break;
                default:
                    showShortToast("Error button click event with id:" + v.getId());
                    break;
            }
        }
    };

    private void clear() {
        log = "";
        resultTextView.setText("");
    }

    private void testGetApis() {
        if (apiName == null) {
            showShortToast("Error: cannot get api name!");
            return;
        }

        ApiList api = ApiList.toApi(apiName);
        switch (api) {
            case JOKE:
                testJokeGet();
                break;
            case RECIPEPUPPY:
                testRecipePuppy("");
            case OMDB:
                testOMDb(getRandomCharacter());
                break;
        }
    }

    private void testQueryApis(String keyword) {
        if (keyword == null) {
            showShortToast("Please enter any keyword...");
            return;
        }

        if (apiName == null) {
            showShortToast("Error: cannot get api name!");
            return;
        }

        ApiList api = ApiList.toApi(apiName);
        switch (api) {
            case JOKE:
                testJokeQuery(keyword);
                break;
            case RECIPEPUPPY:
                testRecipePuppy(keyword);
                break;
            case OMDB:
                testOMDb(keyword);
                break;
        }
    }

    // region JOKE API
    private void testJokeGet() {
        fetchSingleJoke();
        fetchTwoPartJoke();
    }

    private void testJokeQuery(String keyword) {
        fetchSingleJokeBy(keyword);
        fetchTwoPartJokeBy(keyword);
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
    // endregion

    // region RECIPEPUPPY API
    private void testRecipePuppy(String keyword) {
        RecipePuppyInterface api = retrofitRecipePuppy.create(RecipePuppyInterface.class);
        Call<RecipePuppyResponse> call = api.getRecipePuppyResponse("", keyword, 1);
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
    // endregion

    // region OMDB API
    private void testOMDb(String keyword) {
        OMDbInterface api = retrofitOMDb.create(OMDbInterface.class);
        Call<OMDb> call = api.getOMDb(keyword, OMDB_APIKEY);
        call.enqueue(new Callback<OMDb>() {
            @Override
            public void onResponse(Call<OMDb> call, Response<OMDb> response) {
                if (!response.isSuccessful()) {
                    log += "Response Code: " + response.code();
                    resultTextView.setText(log);
                    return;
                }

                OMDb omDb = response.body();

                if (omDb != null) {
                    resultTextView.setText(omDb.toString());
                } else {
                    resultTextView.setText("OMDb is null!");
                }
            }

            @Override
            public void onFailure(Call<OMDb> call, Throwable t) {
                log += "Error: " + t.getMessage();
                resultTextView.setText(log);
            }
        });
    }

    private String getRandomCharacter() {
        Random rnd = new Random();
        char c = (char) (rnd.nextInt(26) + 'a');
        return String.valueOf(c);
    }
    // endregion
}
