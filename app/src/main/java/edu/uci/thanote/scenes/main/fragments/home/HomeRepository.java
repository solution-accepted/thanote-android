package edu.uci.thanote.scenes.main.fragments.home;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.joke.JokeAPIInterface;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.category.CategoryTable;
import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.databases.note.NoteTable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;
import java.util.function.Consumer;

public class HomeRepository {

    // database tables
    private CategoryTable categoryTable;
    private NoteTable noteTable;

    // data fields
    private LiveData<List<Category>> categories;
    private LiveData<List<Note>> notes;

    // api
    private JokeAPIInterface jokeAPIs;

    public HomeRepository(Application application) {
        categoryTable = new CategoryTable(application);
        categories = categoryTable.getCategories();

        noteTable = new NoteTable(application);
        notes = noteTable.getNotes();

        Retrofit jokeRetrofit = APIClient.getInstance().getRetrofitJoke();
        jokeAPIs = jokeRetrofit.create(JokeAPIInterface.class);
    }

    // region Public Methods (Local Database)

    public void insertCategory(Category category) {
        categoryTable.insert(category);
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void insertNote(Note note) {
        noteTable.insert(note);
    }

    public void deleteNote(Note note) {
        noteTable.delete(note);
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    // endregion

    // region Public Methods (Remote API)

    public interface Listener {
        void didFetchError(String message);

        void didFetchSingleJoke(SingleJoke joke);

        void didFetchTwoPartJoke(TwoPartJoke joke);

        void didFetchSingleJokeByKey(SingleJoke joke);

        void didFetchTwoPartJokeByKey(TwoPartJoke joke);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void fetchSingleJoke() {
        jokeAPIs.getSingleJoke()
                .enqueue(getCallback(listener::didFetchSingleJoke));
    }

    public void fetchTwoPartJoke() {
        jokeAPIs.getTwoPartJoke()
                .enqueue(getCallback(listener::didFetchTwoPartJoke));
    }

    public void fetchSingleJokeBy(String key) {
        jokeAPIs.getSingleJokeBy(key)
                .enqueue(getCallback(listener::didFetchSingleJokeByKey));
    }

    public void fetchTwoPartJokeBy(String key) {
        jokeAPIs.getTwoPartJokeBy(key)
                .enqueue(getCallback(listener::didFetchTwoPartJokeByKey));
    }

    private <T> Callback<T> getCallback(Consumer<T> function) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    function.accept(response.body());
                } else {
                    listener.didFetchError("Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                listener.didFetchError(t.getMessage());
            }
        };
    }

    // endregion
}
