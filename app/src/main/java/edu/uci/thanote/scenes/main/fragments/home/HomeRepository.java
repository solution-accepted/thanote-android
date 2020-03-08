package edu.uci.thanote.scenes.main.fragments.home;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.joke.JokeAPIInterface;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.omdb.OMDbInterface;
import edu.uci.thanote.apis.omdb.OMDbMovie;
import edu.uci.thanote.apis.omdb.OMDbMovieSearchResponse;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyInterface;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
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

    // api
    private JokeAPIInterface jokeApi;
    private RecipePuppyInterface recipePuppyApi;
    private OMDbInterface omdbApi;
    private final String OMDB_API_KEY = "7c782685"; // Please DONT abuse this!

    public HomeRepository(Application application) {
        categoryTable = new CategoryTable(application);
        categories = categoryTable.getCategories();

        noteTable = new NoteTable(application);
        notes = noteTable.getNotes();

        jokeApi = APIClient.getInstance().getRetrofitJoke().create(JokeAPIInterface.class);
        recipePuppyApi = APIClient.getInstance().getRetrofitRecipePuppy().create(RecipePuppyInterface.class);
        omdbApi = APIClient.getInstance().getRetrofitOMDb().create(OMDbInterface.class);
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
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void fetchSingleJokeFromApiRandomly() {
        jokeApi.getSingleJoke()
                .enqueue(getCallback(listener::didFetchSingleJokeRandomly));
    }

    public void fetchTwoPartJokeFromApiRandomly() {
        jokeApi.getTwoPartJoke()
                .enqueue(getCallback(listener::didFetchTwoPartJokeRandomly));
    }

    public void fetchSingleJokeFromApiBy(String key) {
        jokeApi.getSingleJokeBy(key)
                .enqueue(getCallback(listener::didFetchSingleJokeByKey));
    }

    public void fetchTwoPartJokeFromApiBy(String key) {
        jokeApi.getTwoPartJokeBy(key)
                .enqueue(getCallback(listener::didFetchTwoPartJokeByKey));
    }

    public void fetchPuppyRecipesFromApiRandomly() {
        recipePuppyApi
                .getRecipePuppyResponse("", "", RecipePuppyInterface.getRandomPageNumber())
                .enqueue(getCallback(listener::didFetchPuppyRecipesRandomly));
    }

    public void fetchPuppyRecipesFromApiBy(String ingredients, String query, int page) {
        if (page < 1 || page > 100) {
            Log.e(TAG, "fetchPuppyRecipes: page number should be in [1, 100]");
            Log.e(TAG, "fetchPuppyRecipes: illegal page number = " + page, new IllegalArgumentException());
        }
        recipePuppyApi
                .getRecipePuppyResponse(ingredients, query, page)
                .enqueue(getCallback(listener::didFetchPuppyRecipesByParams));
    }

    private void fetchOpenMovieFromApiRandomly() {
        // Not Provided
    }

    public void fetchOpenMovieFromApiByTitle(String title) {
        omdbApi.getOMDbMovieByTitle(OMDB_API_KEY, title)
                .enqueue(getCallback(listener::didFetchOpenMovie));
    }

    public void fetchOpenMovieFromApiBySearching(String title) {
        omdbApi.getOMDbMovieBySearching(OMDB_API_KEY, title, 1)
                .enqueue(getCallback(listener::didFetchOpenMovieSearch));
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
