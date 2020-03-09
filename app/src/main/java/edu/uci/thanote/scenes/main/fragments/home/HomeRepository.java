package edu.uci.thanote.scenes.main.fragments.home;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.Api;
import edu.uci.thanote.apis.joke.JokeApi;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.omdb.OMDbApi;
import edu.uci.thanote.apis.omdb.OMDbMovie;
import edu.uci.thanote.apis.omdb.OMDbMovieSearchResponse;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyApi;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.apis.thecocktaildb.CocktailResponse;
import edu.uci.thanote.apis.thecocktaildb.TheCocktailDbApi;
import edu.uci.thanote.apis.themoviedb.TMDbMoviesResponse;
import edu.uci.thanote.apis.themoviedb.TheMovieDbApi;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.category.CategoryTable;
import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.databases.note.NoteTable;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.function.Consumer;

public class HomeRepository {

    private final String TAG = "HomeRepository";

    // database tables
    private CategoryTable categoryTable;
    private NoteTable noteTable;

    // data fields
    private LiveData<List<Category>> categories;
    private LiveData<List<Note>> notes;

    private JokeApi jokeApi;
    private RecipePuppyApi recipePuppyApi;
    private OMDbApi omdbApi;
    private final String OMDB_API_KEY = Api.OMDB.getApiKey();
    private TheMovieDbApi theMovieDbApi;
    private final String TMDB_API_KEY = Api.THEMOVIEDB.getApiKey();
    private TheCocktailDbApi theCocktailDbApi;

    public HomeRepository(Application application) {
        categoryTable = new CategoryTable(application);
        categories = categoryTable.getCategories();

        noteTable = new NoteTable(application);
        notes = noteTable.getNotes();

        // apis
        APIClient apiClient = APIClient.getInstance();
        jokeApi = apiClient.getRetrofitJoke().create(JokeApi.class);
        recipePuppyApi = apiClient.getRetrofitRecipePuppy().create(RecipePuppyApi.class);
        omdbApi = apiClient.getRetrofitOMDb().create(OMDbApi.class);
        theMovieDbApi = apiClient.getRetrofitTheMovieDb().create(TheMovieDbApi.class);
        theCocktailDbApi = apiClient.getRetrofitTheCocktailDb().create(TheCocktailDbApi.class);
    }

    // region Public Methods (Local Database)

    public void insertCategoryIntoDatabase(Category category) {
        categoryTable.insert(category);
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void insertNoteIntoDatabase(Note note) {
        noteTable.insert(note);
    }

    public void deleteNoteFromDatabase(Note note) {
        noteTable.delete(note);
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    // endregion

    // region Public Methods (Remote API)

    public interface Listener {
        void didFetchError(String message);

        void didFetchSingleJokeRandomly(SingleJoke joke);

        void didFetchTwoPartJokeRandomly(TwoPartJoke joke);

        void didFetchSingleJokeByKey(SingleJoke joke);

        void didFetchTwoPartJokeByKey(TwoPartJoke joke);

        void didFetchPuppyRecipesRandomly(RecipePuppyResponse recipes);

        void didFetchPuppyRecipesByParams(RecipePuppyResponse recipes);

        void didFetchOpenMovie(OMDbMovie movie);

        void didFetchOpenMovieSearch(OMDbMovieSearchResponse movies);

        void didFetchTMDBMovieRandomly(TMDbMoviesResponse moviesResponse);

        void didFetchTMDBMovieBySearching(TMDbMoviesResponse moviesResponse);

        void didFetchCocktailRandomly(CocktailResponse cocktailResponse);

        void didFetchCocktailBySearching(CocktailResponse cocktailResponse);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void fetchSingleJokeRandomly() {
        jokeApi.getSingleJoke()
                .enqueue(getCallback(listener::didFetchSingleJokeRandomly));
    }

    public void fetchTwoPartJokeRandomly() {
        jokeApi.getTwoPartJoke()
                .enqueue(getCallback(listener::didFetchTwoPartJokeRandomly));
    }

    public void fetchSingleJokeBy(String key) {
        jokeApi.getSingleJokeBy(key)
                .enqueue(getCallback(listener::didFetchSingleJokeByKey));
    }

    public void fetchTwoPartJokeBy(String key) {
        jokeApi.getTwoPartJokeBy(key)
                .enqueue(getCallback(listener::didFetchTwoPartJokeByKey));
    }

    public void fetchPuppyRecipesRandomly() {
        recipePuppyApi
                .getRecipePuppyResponse("", "", RecipePuppyApi.getRandomPageNumber())
                .enqueue(getCallback(listener::didFetchPuppyRecipesRandomly));
    }

    public void fetchPuppyRecipesBy(String ingredients, String query, int page) {
        if (page < 1 || page > 100) {
            Log.e(TAG, "fetchPuppyRecipes: page number should be in [1, 100]");
            Log.e(TAG, "fetchPuppyRecipes: illegal page number = " + page, new IllegalArgumentException());
        }
        recipePuppyApi
                .getRecipePuppyResponse(ingredients, query, page)
                .enqueue(getCallback(listener::didFetchPuppyRecipesByParams));
    }

    private void fetchOpenMovieRandomly() {
        // Not Provided
    }

    public void fetchOpenMovieByTitle(String title) {
        omdbApi.getOMDbMovieByTitle(OMDB_API_KEY, title)
                .enqueue(getCallback(listener::didFetchOpenMovie));
    }

    public void fetchOpenMovieBySearching(String title) {
        omdbApi.getOMDbMovieBySearching(OMDB_API_KEY, title, 1)
                .enqueue(getCallback(listener::didFetchOpenMovieSearch));
    }

    public void fetchTMDBMovieRandomly() {
        theMovieDbApi.getPopularMovies(TMDB_API_KEY, TheMovieDbApi.getRandomPageNumber())
                .enqueue(getCallback(listener::didFetchTMDBMovieRandomly));
    }

    public void fetchTMDBMovieBySearching(String title) {
        theMovieDbApi.getMovieBySearching(TMDB_API_KEY, title)
                .enqueue(getCallback(listener::didFetchTMDBMovieBySearching));
    }

    public void fetchCocktailRandomly() {
        theCocktailDbApi.getRandomCocktail()
                .enqueue(getCallback(listener::didFetchCocktailRandomly));
    }

    public void fetchCocktailBySearching(String query) {
        theCocktailDbApi.getCocktailBySearching(query)
                .enqueue(getCallback(listener::didFetchCocktailBySearching));
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
