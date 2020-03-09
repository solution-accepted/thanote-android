package edu.uci.thanote.scenes.main;

import android.app.Application;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.Api;
import edu.uci.thanote.apis.joke.JokeApi;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyApi;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.apis.thecocktaildb.CocktailResponse;
import edu.uci.thanote.apis.thecocktaildb.TheCocktailDbApi;
import edu.uci.thanote.apis.themoviedb.TMDbMoviesResponse;
import edu.uci.thanote.apis.themoviedb.TheMovieDbApi;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.function.Consumer;

public class MainRepository {
    private MainRepositoryListener listener;

    // apis
    private final APIClient apiClient = APIClient.getInstance();
    private JokeApi jokeApi;
    private RecipePuppyApi recipePuppyApi;
    private TheMovieDbApi theMovieDbApi;
    private final String TMDB_API_KEY = Api.THEMOVIEDB.getApiKey();
    private TheCocktailDbApi theCocktailDbApi;


    public MainRepository(Application application) {
        // initial apis
        jokeApi = apiClient.getRetrofitJoke().create(JokeApi.class);
        recipePuppyApi = apiClient.getRetrofitRecipePuppy().create(RecipePuppyApi.class);
        theMovieDbApi = apiClient.getRetrofitTheMovieDb().create(TheMovieDbApi.class);
        theCocktailDbApi = apiClient.getRetrofitTheCocktailDb().create(TheCocktailDbApi.class);
    }

    // region Public APIs (API)

    public interface MainRepositoryListener {
        void didFetchError(String message);

        void didFetchSingleJokeRandomly(SingleJoke joke);

        void didFetchPuppyRecipesRandomly(RecipePuppyResponse recipes);

        void didFetchTMDBMovieRandomly(TMDbMoviesResponse movies);

        void didFetchCocktailRandomly(CocktailResponse cocktail);
    }

    public void setListener(MainRepositoryListener listener) {
        this.listener = listener;
    }

    public void fetchSingleJokeRandomly() {
        jokeApi.getSingleJoke()
                .enqueue(getCallback(listener::didFetchSingleJokeRandomly));
    }


    public void fetchPuppyRecipesRandomly() {
        recipePuppyApi
                .getRecipePuppyResponse("", "", RecipePuppyApi.getRandomPageNumber())
                .enqueue(getCallback(listener::didFetchPuppyRecipesRandomly));
    }


    public void fetchTMDBMovieRandomly() {
        theMovieDbApi.getPopularMovies(TMDB_API_KEY, TheMovieDbApi.getRandomPageNumber())
                .enqueue(getCallback(listener::didFetchTMDBMovieRandomly));
    }

    public void fetchCocktailRandomly() {
        theCocktailDbApi.getRandomCocktail()
                .enqueue(getCallback(listener::didFetchCocktailRandomly));
    }

    private <T> Callback<T> getCallback(Consumer<T> function) {
        return new Callback<T>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.isSuccessful()) {
                    function.accept(response.body());
                } else {
                    listener.didFetchError("Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                listener.didFetchError(t.getMessage());
            }
        };
    }

    // endregion
}
