package edu.uci.thanote.scenes.main;

import android.app.Application;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.joke.JokeApi;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.recipepuppy.Recipe;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyApi;
import edu.uci.thanote.apis.thecocktaildb.Cocktail;
import edu.uci.thanote.apis.thecocktaildb.TheCocktailDbApi;
import edu.uci.thanote.apis.themoviedb.Movie;
import edu.uci.thanote.apis.themoviedb.TheMovieDbApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainRepository {
    private MainRepositoryListener listener;

    // apis
    private JokeApi jokeApi;
    private RecipePuppyApi recipeApi;
    private TheMovieDbApi movieApi;
    private TheCocktailDbApi cocktailApi;

    public MainRepository(Application application) {
        // initial apis
        jokeApi = APIClient.getInstance().getRetrofitJoke().create(JokeApi.class);
        recipeApi = APIClient.getInstance().getRetrofitRecipePuppy().create(RecipePuppyApi.class);
        movieApi = APIClient.getInstance().getRetrofitTheMovieDb().create(TheMovieDbApi.class);
        cocktailApi = APIClient.getInstance().getRetrofitTheCocktailDb().create(TheCocktailDbApi.class);
    }

    // region Public APIs (API)

    public interface MainRepositoryListener {
        void didFetchError(String message);
        void didFetchSingleJoke(SingleJoke joke);
        void didFetchRecipe(Recipe recipe);
        void didFetchMovie(Movie movie);
        void didFetchCocktail(Cocktail cocktail);
    }

    public void setListener(MainRepositoryListener listener) {
        this.listener = listener;
    }

    // TODO: - Junxian! Please help to finish api calls for fetchSingleJoke()
    public void fetchSingleJoke() {
        Call<SingleJoke> call = jokeApi.getSingleJoke();
        call.enqueue(new Callback<SingleJoke>() {
            @Override
            public void onResponse(Call<SingleJoke> call, Response<SingleJoke> response) {
                if (!response.isSuccessful()) {
                    listener.didFetchError("Response Code: " + response.code());
                    return;
                }

                listener.didFetchSingleJoke(response.body());
            }

            @Override
            public void onFailure(Call<SingleJoke> call, Throwable t) {
                listener.didFetchError(t.getMessage());
            }
        });
    }

    // TODO: - Junxian! Please help to finish api calls for fetchRecipe()
    public void fetchRecipe() {
        // only for test
        listener.didFetchRecipe(null);
    }

    // TODO: - Junxian! Please help to finish api calls for fetchMovie()
    public void fetchMovie() {
        // only for test
        listener.didFetchMovie(null);
    }

    // TODO: - Junxian! Please help to finish api calls for fetchCocktail()
    public void fetchCocktail() {
        // only for test
        listener.didFetchCocktail(null);
    }

    // endregion
}
