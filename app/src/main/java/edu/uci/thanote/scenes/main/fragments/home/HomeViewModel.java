package edu.uci.thanote.scenes.main.fragments.home;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.uci.thanote.apis.BasicNote;
import edu.uci.thanote.apis.ImageNote;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.nasa.NasaApod;
import edu.uci.thanote.apis.numbers.Number;
import edu.uci.thanote.apis.openmoviedb.OMDbMovie;
import edu.uci.thanote.apis.openmoviedb.OMDbMovieSearchResponse;
import edu.uci.thanote.apis.opentriviadb.TriviaResponse;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyApi;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.apis.thecocktaildb.CocktailResponse;
import edu.uci.thanote.apis.themoviedb.TMDbMoviesResponse;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.note.Note;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeViewModel extends AndroidViewModel {

    private final String TAG = "HomeViewModel";

    // model = local database + remote api
    private final HomeRepository repository;

    // view model = local user interface
    private LiveData<List<Category>> categoriesInDatabase;
    private LiveData<List<Note>> notesInDatabase;
    private MutableLiveData<List<Note>> notesInMemory;
    private List<Note> notesInMemoryBackup;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new HomeRepository(application);
        repository.setListener(repoListener);
        categoriesInDatabase = repository.getCategories();
        notesInDatabase = repository.getNotes();
        notesInMemory = new MutableLiveData<>();
    }

    // region Public APIs

    public void insertCategoryIntoDatabase(Category category) {
        if (!isValidCategory(category)) {
            listener.didVerifyError("some insert error occur...");
            return;
        }
        repository.insertCategoryIntoDatabase(category);
    }

    public LiveData<List<Category>> getCategoriesInDatabase() {
        return categoriesInDatabase;
    }

    public void insertNoteIntoDatabase(Note note) {
        repository.insertNoteIntoDatabase(note);
    }

    public void deleteNoteFromDatabase(Note note) {
        repository.deleteNoteFromDatabase(note);
    }

    public LiveData<List<Note>> getNotesInDatabase() {
        return notesInDatabase;
    }

    public MutableLiveData<List<Note>> getNotesInMemory() {
        return notesInMemory;
    }

    public void insertNoteIntoMemory(Note note) {
        List<Note> newNotes = notesInMemory.getValue();
        if (Objects.requireNonNull(newNotes).add(note)) {
            notesInMemory.setValue(newNotes);
        } else {
            Log.e(TAG, "insertNoteIntoMemory: Failed to insert note = " + note.toString());
        }
    }

    public void insertNoteIntoMemory(String title, String detail, String imageUrl) {
        insertNoteIntoMemory(new Note(title, detail, Category.DEFAULT_CATEGORY_ID, imageUrl));
    }

    public void insertNoteIntoMemory(String title, String detail) {
        insertNoteIntoMemory(new Note(title, detail, Category.DEFAULT_CATEGORY_ID, ""));
    }

    public void insertNoteIntoMemory(@NotNull BasicNote note) {
        insertNoteIntoMemory(note.getTitle(), note.getDetail());
    }

    public void insertNoteIntoMemory(@NotNull ImageNote note) {
        insertNoteIntoMemory(note.getTitle(), note.getDetail(), note.getImageUrl());
    }

    public void setNotesInMemory(List<Note> notes) {
        notesInMemory.setValue(notes);
    }

    public void deleteNotesFromMemory() {
        notesInMemory.setValue(new ArrayList<>());
    }

    public void backupNotesInMemory() {
        notesInMemoryBackup = notesInMemory.getValue();
    }

    public void restoreNotesInMemory() {
        setNotesInMemory(notesInMemoryBackup);
    }

    public void fetchSingleJoke() {
        repository.fetchSingleJokeRandomly();
    }

    public void fetchTwoPartJoke() {
        repository.fetchTwoPartJokeRandomly();
    }

    public void searchSingleJoke(String key) {
        repository.fetchSingleJokeBy(key);
    }

    public void searchTwoPartJoke(String key) {
        repository.fetchTwoPartJokeBy(key);
    }

    public void fetchPuppyRecipes(String ingredients, String query, int page) {
        repository.fetchPuppyRecipesBy(ingredients, query, page);
    }

    public void fetchPuppyRecipesRandomly() {
        repository.fetchPuppyRecipesRandomly();
    }

    public void searchPuppyRecipes(String query) {
        fetchPuppyRecipes("", query, RecipePuppyApi.getRandomPageNumber());
    }

    public void searchOMDBMovie(String query) {
        repository.fetchOMDBMovieByTitle(query);
        repository.fetchOMDBMovieBySearching(query);
    }

    public void fetchTMDBMovieRandomly() {
        repository.fetchTMDBMovieRandomly();
    }

    public void searchTMDBMovie(String query) {
        repository.fetchTMDBMovieBySearching(query);
    }

    public void fetchCocktailRandomly() {
        repository.fetchCocktailRandomly();
    }

    public void searchCocktail(String query) {
        repository.fetchCocktailBySearching(query);
    }

    // TODAY only, not random
    public void fetchNasaApodToday() {
        repository.fetchNasaApodToday();
    }

    public void fetchNasaApodRandomly() {
        repository.fetchNasaApodRandomly();
    }

    public void searchNasaApod(String query) {
        repository.fetchNasaApodBySearching(query);
    }

    public void fetchNumberRandomly() {
        repository.fetchNumberRandomly();
    }

    public void searchNumber(String query) {
        repository.fetchNumberRandomlyBySearching(query);
    }

    public void fetchTiviaRandomly() {
        repository.fetchTriviaRandomly();
    }

    public void searchTrivia(String query) {
        repository.fetchTriviaByAmount(query);
    }

    // endregion

    // region ViewModel Listener

    private Listener listener;

    public interface Listener {
        void didFetchSingleJokeRandomly(SingleJoke joke);

        void didFetchTwoPartJokeRandomly(TwoPartJoke joke);

        void didFetchSingleJokeByKey(SingleJoke joke);

        void didFetchTwoPartJokeByKey(TwoPartJoke joke);

        void didFetchPuppyRecipesRandomly(RecipePuppyResponse recipes);

        void didFetchPuppyRecipesByParams(RecipePuppyResponse recipes);

        void didFetchError(String message);

        void didVerifyError(String message);

        void didFetchOMDBMovie(OMDbMovie movie);

        void didFetchOMDBMovieSearch(OMDbMovieSearchResponse movies);

        void didFetchTMDBMovieRandomly(TMDbMoviesResponse movies);

        void didFetchTMDBMovieBySearching(TMDbMoviesResponse movies);

        void didFetchCocktailRandomly(CocktailResponse cocktails);

        void didFetchCocktailBySearching(CocktailResponse cocktails);

        void didFetchNasaApodRandomly(NasaApod nasaApod);

        void didFetchNumber(Number number);

        void didFetchTriviaRandomly(TriviaResponse trivia);

        void didFetchTriviaList(TriviaResponse triviaResponse);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private final HomeRepository.Listener repoListener = new HomeRepository.Listener() {

        // If HomeRepository.Listener didFetchError
        // Then HomeViewModel.Listener didFetchError
        // Then HomeFragment didFetchError

        // So HomeRepository.Listener and HomeViewModel.Listener do nothing
        // They are just passing the message to HomeFragment

        @Override
        public void didFetchError(String message) {
            listener.didFetchError(message);
        }

        @Override
        public void didFetchSingleJokeRandomly(SingleJoke joke) {
            listener.didFetchSingleJokeRandomly(joke);
        }

        @Override
        public void didFetchTwoPartJokeRandomly(TwoPartJoke joke) {
            listener.didFetchTwoPartJokeRandomly(joke);
        }

        @Override
        public void didFetchSingleJokeByKey(SingleJoke joke) {
            listener.didFetchSingleJokeByKey(joke);
        }

        @Override
        public void didFetchTwoPartJokeByKey(TwoPartJoke joke) {
            listener.didFetchTwoPartJokeByKey(joke);
        }

        @Override
        public void didFetchPuppyRecipesRandomly(RecipePuppyResponse recipes) {
            listener.didFetchPuppyRecipesRandomly(recipes);
        }

        @Override
        public void didFetchPuppyRecipesByParams(RecipePuppyResponse recipes) {
            listener.didFetchPuppyRecipesByParams(recipes);
        }

        @Override
        public void didFetchOMDBMovie(OMDbMovie movie) {
            listener.didFetchOMDBMovie(movie);
        }

        @Override
        public void didFetchOMDBMovieSearch(OMDbMovieSearchResponse movies) {
            listener.didFetchOMDBMovieSearch(movies);
        }

        @Override
        public void didFetchTMDBMovieRandomly(TMDbMoviesResponse movies) {
            listener.didFetchTMDBMovieRandomly(movies);
        }

        @Override
        public void didFetchTMDBMovieBySearching(TMDbMoviesResponse movies) {
            listener.didFetchTMDBMovieBySearching(movies);
        }

        @Override
        public void didFetchCocktailRandomly(CocktailResponse cocktails) {
            listener.didFetchCocktailRandomly(cocktails);
        }

        @Override
        public void didFetchCocktailBySearching(CocktailResponse cocktails) {
            listener.didFetchCocktailBySearching(cocktails);
        }

        @Override
        public void didFetchNasaApod(NasaApod nasaApod) {
            listener.didFetchNasaApodRandomly(nasaApod);
        }

        @Override
        public void didFetchNumber(Number number) {
            listener.didFetchNumber(number);
        }

        @Override
        public void didFetchTriviaRandomly(TriviaResponse trivia) {
            listener.didFetchTriviaRandomly(trivia);
        }

        @Override
        public void didFetchTriviaList(TriviaResponse triviaResponse) {
            listener.didFetchTriviaList(triviaResponse);
        }
    };

    // endregion

    private boolean isValidCategory(Category category) {
        return !category.getName().isEmpty();
    }

}
