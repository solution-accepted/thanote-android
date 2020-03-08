package edu.uci.thanote.scenes.main.fragments.home;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.omdb.OMDbMovie;
import edu.uci.thanote.apis.omdb.OMDbMovieSearchResponse;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyApi;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.note.Note;

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

    public void setNotesInMemory(List<Note> notes) {
        notesInMemory.setValue(notes);
    }

    public void deleteNotesFromMemory() {
        notesInMemory.setValue(new ArrayList<>());
    }

    public void fetchSingleJokeFromApi() {
        repository.fetchSingleJokeFromApiRandomly();
    }

    public void fetchTwoPartJokeFromApi() {
        repository.fetchTwoPartJokeFromApiRandomly();
    }

    public void searchSingleJoke(String key) {
        repository.fetchSingleJokeFromApiBy(key);
    }

    public void searchTwoPartJoke(String key) {
        repository.fetchTwoPartJokeFromApiBy(key);
    }

    public void fetchPuppyRecipesFromApi(String ingredients, String query, int page) {
        repository.fetchPuppyRecipesFromApiBy(ingredients, query, page);
    }                                                                                                                                                                                                                                                                                                                    

    public void fetchPuppyRecipesFromApiRandomly() {
        repository.fetchPuppyRecipesFromApiRandomly();
    }

    public void searchPuppyRecipes(String query) {
        fetchPuppyRecipesFromApi("", query, RecipePuppyApi.getRandomPageNumber());
    }

    public void searchOpenMovie(String query) {
        repository.fetchOpenMovieFromApiByTitle(query);
        repository.fetchOpenMovieFromApiBySearching(query);
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

        void didFetchOpenMovie(OMDbMovie movie);

        void didFetchOpenMovieSearch(OMDbMovieSearchResponse movies);
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
        public void didFetchOpenMovie(OMDbMovie movie) {
            listener.didFetchOpenMovie(movie);
        }

        @Override
        public void didFetchOpenMovieSearch(OMDbMovieSearchResponse movies) {
            listener.didFetchOpenMovieSearch(movies);
        }
    };

    // endregion

    private boolean isValidCategory(Category category) {
        return !category.getName().isEmpty();
    }

}
