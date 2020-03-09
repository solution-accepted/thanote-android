package edu.uci.thanote.scenes.test.api;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.uci.thanote.R;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.Api;
import edu.uci.thanote.apis.joke.JokeApi;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.omdb.OMDbApi;
import edu.uci.thanote.apis.omdb.OMDbMovie;
import edu.uci.thanote.apis.recipepuppy.Recipe;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyApi;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.apis.thecocktaildb.Cocktail;
import edu.uci.thanote.apis.thecocktaildb.CocktailResponse;
import edu.uci.thanote.apis.thecocktaildb.TheCocktailDbApi;
import edu.uci.thanote.apis.themoviedb.TMDbMovie;
import edu.uci.thanote.apis.themoviedb.TMDbMoviesResponse;
import edu.uci.thanote.apis.themoviedb.TheMovieDbApi;
import edu.uci.thanote.scenes.general.BaseActivity;
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

    // components
    private EditText queryEditText;
    private TextView resultTextView;
    private TextView titleTextView;

    // retrofits
    private Retrofit retrofitJoke = APIClient.getInstance().getRetrofitJoke();
    private Retrofit retrofitRecipePuppy = APIClient.getInstance().getRetrofitRecipePuppy();
    private Retrofit retrofitOMDb = APIClient.getInstance().getRetrofitOMDb();
    private Retrofit retrofitTheMovieDb = APIClient.getInstance().getRetrofitTheMovieDb();
    private Retrofit retrofitTheCocktailDb = APIClient.getInstance().getRetrofitTheCocktailDb();

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

        Api api = Api.toApi(apiName);
        switch (api) {
            case JOKE:
                testJokeGet();
                break;
            case RECIPEPUPPY:
                testRecipePuppy("");
            case OMDB:
                testOMDb(getRandomCharacter());
                break;
            case THEMOVIEDB:
                testTheMovieDbGet();
                break;
            case THECOCKTAILDB:
                testTheCocktailDbGet();
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

        Api api = Api.toApi(apiName);
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
            case THEMOVIEDB:
                testTheMovieDbQuery(keyword);
                break;
            case THECOCKTAILDB:
                testTheCocktailDbQuery(keyword);
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
        JokeApi api = retrofitJoke.create(JokeApi.class);
        Call<SingleJoke> call = api.getSingleJoke();
        singleJokeCall(call);
    }

    private void fetchTwoPartJoke() {
        JokeApi api = retrofitJoke.create(JokeApi.class);
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
        JokeApi api = retrofitJoke.create(JokeApi.class);
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
        JokeApi api = retrofitJoke.create(JokeApi.class);
        Call<TwoPartJoke> call = api.getTwoPartJokeBy(key);
        twoPartJokeCall(call);
    }
    // endregion

    // region RECIPEPUPPY API
    private void testRecipePuppy(String keyword) {
        RecipePuppyApi api = retrofitRecipePuppy.create(RecipePuppyApi.class);
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
        OMDbApi api = retrofitOMDb.create(OMDbApi.class);
        Call<OMDbMovie> call = api.getOMDbMovieByTitle(keyword, Api.OMDB.getApiKey());
        call.enqueue(new Callback<OMDbMovie>() {
            @Override
            public void onResponse(Call<OMDbMovie> call, Response<OMDbMovie> response) {
                if (!response.isSuccessful()) {
                    log += "Response Code: " + response.code();
                    resultTextView.setText(log);
                    return;
                }

                OMDbMovie omDbMovie = response.body();

                if (omDbMovie != null) {
                    resultTextView.setText(omDbMovie.toString());
                } else {
                    resultTextView.setText("OMDb is null!");
                }
            }

            @Override
            public void onFailure(Call<OMDbMovie> call, Throwable t) {
                log += "Error: " + t.getMessage();
                resultTextView.setText(log);
            }
        });
    }

    // endregion

    // region TheMovieDb API
    private void testTheMovieDbGet() {
        TheMovieDbApi api = retrofitTheMovieDb.create(TheMovieDbApi.class);
        Call<TMDbMoviesResponse> call = api.getTopRatedMovies(Api.THEMOVIEDB.getApiKey());
        executeTheMovieDb(call);
    }

    private void testTheMovieDbQuery(String keyword) {
        TheMovieDbApi api = retrofitTheMovieDb.create(TheMovieDbApi.class);
        Call<TMDbMoviesResponse> call = api.getMovieBySearching(Api.THEMOVIEDB.getApiKey(), keyword);
        executeTheMovieDb(call);
    }

    private void executeTheMovieDb(Call<TMDbMoviesResponse> call) {
        call.enqueue(new Callback<TMDbMoviesResponse>() {
            @Override
            public void onResponse(Call<TMDbMoviesResponse> call, Response<TMDbMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    log += "Response Code: " + response.code();
                    resultTextView.setText(log);
                    return;
                }

                List<TMDbMovie> movies = response.body().getMovies();

                if (movies != null) {
                    StringBuilder sb = new StringBuilder();

                    for (TMDbMovie movie : movies) {
                        sb.append(movie.toString()).append("\n\n");
                    }

                    resultTextView.setText(sb.toString());
                } else {
                    resultTextView.setText("Movies are null!");
                }
            }

            @Override
            public void onFailure(Call<TMDbMoviesResponse> call, Throwable t) {
                log += "Error: " + t.getMessage();
                resultTextView.setText(log);
            }
        });
    }
    // endregion

    // region TheCocktailDb API
    private void testTheCocktailDbGet() {
        TheCocktailDbApi api = retrofitTheCocktailDb.create(TheCocktailDbApi.class);
        Call<CocktailResponse> call = api.getRandomCocktail();
        executeTheCocktailDbApi(call);
    }

    private void testTheCocktailDbQuery(String keyword) {
        TheCocktailDbApi api = retrofitTheCocktailDb.create(TheCocktailDbApi.class);
        Call<CocktailResponse> call = api.queryCocktail(keyword);
        executeTheCocktailDbApi(call);
    }

    private void executeTheCocktailDbApi(Call<CocktailResponse> call) {
        call.enqueue(new Callback<CocktailResponse>() {
            @Override
            public void onResponse(Call<CocktailResponse> call, Response<CocktailResponse> response) {
                if (!response.isSuccessful()) {
                    log += "Response Code: " + response.code();
                    resultTextView.setText(log);
                    return;
                }

                List<Cocktail> cocktails = response.body().getCocktails();

                if (cocktails != null) {
                    StringBuilder sb = new StringBuilder();

                    for (Cocktail cocktail : cocktails) {
                        sb.append(cocktail.toString()).append("\n\n");
                    }

                    resultTextView.setText(sb.toString());
                } else {
                    resultTextView.setText("Movies are null!");
                }
            }

            @Override
            public void onFailure(Call<CocktailResponse> call, Throwable t) {
                log += "Error: " + t.getMessage();
                resultTextView.setText(log);
            }
        });
    }
    // endregion

    private String getRandomCharacter() {
        Random rnd = new Random();
        char c = (char) (rnd.nextInt(26) + 'a');
        return String.valueOf(c);
    }
}
