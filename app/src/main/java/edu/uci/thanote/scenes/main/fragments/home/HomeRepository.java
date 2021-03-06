package edu.uci.thanote.scenes.main.fragments.home;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.Api;
import edu.uci.thanote.apis.joke.JokeApi;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.nasa.NasaApi;
import edu.uci.thanote.apis.nasa.NasaApod;
import edu.uci.thanote.apis.numbers.Number;
import edu.uci.thanote.apis.numbers.NumbersApi;
import edu.uci.thanote.apis.openmoviedb.OMDbApi;
import edu.uci.thanote.apis.openmoviedb.OMDbMovie;
import edu.uci.thanote.apis.openmoviedb.OMDbMovieSearchResponse;
import edu.uci.thanote.apis.opentriviadb.TriviaApi;
import edu.uci.thanote.apis.opentriviadb.TriviaResponse;
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
import java.util.Random;
import java.util.function.Consumer;

public class HomeRepository {

    private final String TAG = "HomeRepository";

    // database tables
    private final CategoryTable categoryTable;
    private final NoteTable noteTable;

    // data fields
    private final LiveData<List<Category>> categories;
    private final LiveData<List<Note>> notes;

    // apis
    private final JokeApi jokeApi;
    private final RecipePuppyApi recipePuppyApi;
    private final OMDbApi omdbApi;
    private final String OMDB_API_KEY = Api.OMDB.getApiKey();
    private final TheMovieDbApi theMovieDbApi;
    private final String TMDB_API_KEY = Api.THEMOVIEDB.getApiKey();
    private final TheCocktailDbApi theCocktailDbApi;
    private final NasaApi nasaApi;
    private final String NASA_API_KEY = Api.NASA.getApiKey();
    private final NumbersApi numbersApi;
    private final TriviaApi triviaApi;

    public HomeRepository(Application application) {
        categoryTable = new CategoryTable(application);
        categories = categoryTable.getCategories();

        noteTable = new NoteTable(application);
        notes = noteTable.getNotes();

        APIClient apiClient = APIClient.getInstance();
        jokeApi = apiClient.getRetrofitJoke().create(JokeApi.class);
        recipePuppyApi = apiClient.getRetrofitRecipePuppy().create(RecipePuppyApi.class);
        omdbApi = apiClient.getRetrofitOMDb().create(OMDbApi.class);
        theMovieDbApi = apiClient.getRetrofitTheMovieDb().create(TheMovieDbApi.class);
        theCocktailDbApi = apiClient.getRetrofitTheCocktailDb().create(TheCocktailDbApi.class);
        nasaApi = apiClient.getRetrofitNasa().create(NasaApi.class);
        numbersApi = apiClient.getRetrofitNumbers().create(NumbersApi.class);
        triviaApi = apiClient.getRetrofitTrivia().create(TriviaApi.class);
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

        void didFetchOMDBMovie(OMDbMovie movie);

        void didFetchOMDBMovieSearch(OMDbMovieSearchResponse movies);

        void didFetchTMDBMovieRandomly(TMDbMoviesResponse moviesResponse);

        void didFetchTMDBMovieBySearching(TMDbMoviesResponse moviesResponse);

        void didFetchCocktailRandomly(CocktailResponse cocktailResponse);

        void didFetchCocktailBySearching(CocktailResponse cocktailResponse);

        void didFetchNasaApod(NasaApod nasaApod);

        void didFetchNumber(Number number);

        void didFetchTriviaRandomly(TriviaResponse triviaResponse);

        void didFetchTriviaList(TriviaResponse triviaResponse);
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

    private void fetchOMDBMovieRandomly() {
        // Not Provided
    }

    public void fetchOMDBMovieByTitle(String title) {
        omdbApi.getOMDbMovieByTitle(OMDB_API_KEY, title)
                .enqueue(getCallback(listener::didFetchOMDBMovie));
    }

    public void fetchOMDBMovieBySearching(String title) {
        omdbApi.getOMDbMovieBySearching(OMDB_API_KEY, title, 1)
                .enqueue(getCallback(listener::didFetchOMDBMovieSearch));
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

    public void fetchNasaApodToday() {
        nasaApi.getAstronomyPictureOfTheDay(NASA_API_KEY)
                .enqueue(getCallback(listener::didFetchNasaApod));
    }

    public void fetchNasaApodRandomly() {
        final int year = 1995 + new Random().nextInt(14); // 1995 - 2019
        final int month = 1 + new Random().nextInt(12); // 1 - 12
        final int day = 1 + new Random().nextInt(28); // at least 28 days a month

        final String sYear = String.valueOf(year);
        final String sMonth = String.valueOf(month).length() != 2 ? "0" + month : String.valueOf(month);
        final String sDay = String.valueOf(day).length() != 2 ? "0" + day : String.valueOf(day);

        final String date = String.join("-", sYear, sMonth, sDay);

        nasaApi.getAstronomyPictureOfTheDay(NASA_API_KEY, true, date)
                .enqueue(getCallback(listener::didFetchNasaApod));
    }

    public void fetchNasaApodBySearching(String query) {
        // 1995-06-16 <= query <= TODAY
        if (query.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")) {
            nasaApi.getAstronomyPictureOfTheDay(NASA_API_KEY, true, query)
                    .enqueue(getCallback(listener::didFetchNasaApod));
        } else {
            listener.didFetchError("Nasa: Try YYYY-MM-DD?");
            fetchNasaApodRandomly();
        }
    }

    public void fetchNumberRandomly() {
        numbersApi.getRandom("trivia")
                .enqueue(getCallback(listener::didFetchNumber));
    }

    public void fetchNumberRandomlyBySearching(String query) {
        try {
            numbersApi.getNumber(Integer.parseInt(query), "trivia")
                    .enqueue(getCallback(listener::didFetchNumber));
        } catch (NumberFormatException e) {
            listener.didFetchError("NumbersAPI: Not a Number");
        }
    }

    public void fetchTriviaRandomly() {
        triviaApi.getSingleMultipleChoiceTrivia()
                .enqueue(getCallback(listener::didFetchTriviaRandomly));
    }

    public void fetchTriviaByAmount(String query) {
        try {
            triviaApi.getMultipleChoiceTrivia(Integer.parseInt(query))
                    .enqueue(getCallback(listener::didFetchTriviaList));
        } catch (NumberFormatException e) {
            listener.didFetchError("TriviaAPI: Try input a number?");
        }
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
