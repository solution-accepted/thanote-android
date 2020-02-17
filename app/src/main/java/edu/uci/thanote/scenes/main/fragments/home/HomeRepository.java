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

public class HomeRepository {

    // database tables
    private CategoryTable categoryTable;
    private NoteTable noteTable;

    // api
    private JokeAPIInterface jokeAPIInterface;

    // data fields
    private LiveData<List<Category>> categories;
    private LiveData<List<Note>> notes;

    public HomeRepository(Application application) {
        categoryTable = new CategoryTable(application);
        categories = categoryTable.getCategories();

        noteTable = new NoteTable(application);
        notes = noteTable.getNotes();

        Retrofit jokeRetrofit = APIClient.getInstance().getRetrofitJoke();
        jokeAPIInterface = jokeRetrofit.create(JokeAPIInterface.class);
    }

    // region Public APIs (Database)

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

    // region Public APIs (API)

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
        jokeAPIInterface
                .getSingleJoke()
                .enqueue(new Callback<SingleJoke>() {
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

    public void fetchTwoPartJoke() {
        Call<TwoPartJoke> call = jokeAPIInterface.getTwoPartJoke();
        call.enqueue(new Callback<TwoPartJoke>() {
            @Override
            public void onResponse(Call<TwoPartJoke> call, Response<TwoPartJoke> response) {
                if (!response.isSuccessful()) {
                    listener.didFetchError("Response Code: " + response.code());
                    return;
                }

                listener.didFetchTwoPartJoke(response.body());
            }

            @Override
            public void onFailure(Call<TwoPartJoke> call, Throwable t) {
                listener.didFetchError(t.getMessage());
            }
        });
    }

    public void fetchSingleJokeBy(String key) {
        Call<SingleJoke> call = jokeAPIInterface.getSingleJokeBy(key);
        call.enqueue(new Callback<SingleJoke>() {
            @Override
            public void onResponse(Call<SingleJoke> call, Response<SingleJoke> response) {
                if (!response.isSuccessful()) {
                    listener.didFetchError("Response Code: " + response.code());
                    return;
                }

                listener.didFetchSingleJokeByKey(response.body());
            }

            @Override
            public void onFailure(Call<SingleJoke> call, Throwable t) {
                listener.didFetchError(t.getMessage());
            }
        });
    }

    public void fetchTwoPartJokeBy(String key) {
        Call<TwoPartJoke> call = jokeAPIInterface.getTwoPartJokeBy(key);
        call.enqueue(new Callback<TwoPartJoke>() {
            @Override
            public void onResponse(Call<TwoPartJoke> call, Response<TwoPartJoke> response) {
                if (!response.isSuccessful()) {
                    listener.didFetchError("Response Code: " + response.code());
                    return;
                }

                listener.didFetchTwoPartJokeByKey(response.body());
            }

            @Override
            public void onFailure(Call<TwoPartJoke> call, Throwable t) {
                listener.didFetchError(t.getMessage());
            }
        });
    }

    // endregion
}
