package edu.uci.thanote.scenes.main.fragments.home;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.note.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HomeViewModel extends AndroidViewModel {

    private final String TAG = "HomeViewModel";

    private final List<Note> NOTES = Arrays.asList(
            new Note("note1", "note1", 1, ""),
            new Note("note2", "note2", 2, ""),
            new Note("note3", "note3", 3, "")
    );

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
        repository.setListener(new HomeRepository.Listener() {

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
            public void didFetchSingleJoke(SingleJoke joke) {
                listener.didFetchSingleJoke(joke);
            }

            @Override
            public void didFetchTwoPartJoke(TwoPartJoke joke) {
                listener.didFetchTwoPartJoke(joke);
            }

            @Override
            public void didFetchSingleJokeByKey(SingleJoke joke) {
                listener.didFetchSingleJokeByKey(joke);
            }

            @Override
            public void didFetchTwoPartJokeByKey(TwoPartJoke joke) {
                listener.didFetchTwoPartJokeByKey(joke);
            }
        });
        categoriesInDatabase = repository.getCategories();
        notesInDatabase = repository.getNotes();
        notesInMemory = new MutableLiveData<>();
    }

    // region Public APIs

    public void insertCategory(Category category) {
        if (!isValidCategory(category)) {
            listener.didVerifyError("some insert error occur...");
            return;
        }
        repository.insertCategory(category);
    }

    public LiveData<List<Category>> getCategoriesInDatabase() {
        return categoriesInDatabase;
    }

    public void insertNote(Note note) {
        repository.insertNote(note);
    }

    public void deleteNote(Note note) {
        repository.deleteNote(note);
    }

    public LiveData<List<Note>> getNotesInDatabase() {
        return notesInDatabase;
    }

    public MutableLiveData<List<Note>> getNotesInMemory() {
        return notesInMemory;
    }

    public void insertNoteInMemory(Note note) {
        List<Note> newNotes = notesInMemory.getValue();
        if (Objects.requireNonNull(newNotes).add(note)) {
            notesInMemory.setValue(newNotes);
        } else {
            Log.e(TAG, "insertNodeInMemory: Failed to insert note = " + note.toString());
        }
    }

    public void setNotesInMemory(List<Note> notes) {
        notesInMemory.setValue(notes);
    }

    public void deleteNotesInMemory() {
        notesInMemory.setValue(new ArrayList<>());
    }

    public void backupNotesInMemory() {
        notesInMemoryBackup = notesInMemory.getValue();
    }

    public void restoreNotesInMemory() {
        setNotesInMemory(notesInMemoryBackup);
    }

    public void getSingleJoke() {
        repository.fetchSingleJoke();
    }

    public void getTwoPartJoke() {
        repository.fetchTwoPartJoke();
    }

    public void searchSingleJoke(String key) {
        repository.fetchSingleJokeBy(key);
    }

    public void searchTwoPartJoke(String key) {
        repository.fetchTwoPartJokeBy(key);
    }

    // endregion

    // region ViewModel Listener

    private Listener listener;

    public interface Listener {
        void didFetchSingleJoke(SingleJoke joke);

        void didFetchTwoPartJoke(TwoPartJoke joke);

        void didFetchSingleJokeByKey(SingleJoke joke);

        void didFetchTwoPartJokeByKey(TwoPartJoke joke);

        void didFetchError(String message);

        void didVerifyError(String message);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    // endregion

    private boolean isValidCategory(Category category) {
        return !category.getName().isEmpty();
    }

}
