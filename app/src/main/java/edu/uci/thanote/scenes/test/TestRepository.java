package edu.uci.thanote.scenes.test;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.demo.Post;
import edu.uci.thanote.apis.demo.PostApi;
import edu.uci.thanote.apis.joke.JokeApi;
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

public class TestRepository {
    private TestRepositoryListener listener;

    // database tables
    private CategoryTable categoryTable;
    private NoteTable noteTable;

    // api
    private PostApi postApi;
    private JokeApi jokeApi;

    // data fields
    private LiveData<List<Category>> categories;
    private LiveData<List<Note>> notes;

    public TestRepository(Application application) {
        // initial database table
        categoryTable = new CategoryTable(application);
        categories = categoryTable.getCategories();

        noteTable = new NoteTable(application);
        notes = noteTable.getNotes();

        // initial retrofit
        Retrofit demoRetrofit = APIClient.getInstance().getRetrofitDemo();
        postApi = demoRetrofit.create(PostApi.class);

        Retrofit jokeRetrofit = APIClient.getInstance().getRetrofitJoke();
        jokeApi = jokeRetrofit.create(JokeApi.class);
    }

    // region Public APIs (Database)

    public void insertCategory(Category category) {
        categoryTable.insert(category);
    }

    public void updateCategory(Category category) {
        categoryTable.update(category);
    }

    public void deleteCategory(Category category) {
        categoryTable.delete(category);
    }

    public void deleteAllCategories() {
        categoryTable.deleteAllCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void insertNote(Note note) {
        noteTable.insert(note);
    }

    public void updateNote(Note note) {
        noteTable.update(note);
    }

    public void deleteNote(Note note) {
        noteTable.delete(note);
    }

    public void deleteAllNotes() {
        noteTable.deleteAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    // endregion

    // region Public APIs (API)

    public interface TestRepositoryListener {
        void didFetchError(String message);
        void didFetchSingleJoke(SingleJoke joke);
        void didFetchTwoPartJoke(TwoPartJoke joke);
        void didFetchSingleJokeByKey(SingleJoke joke);
        void didFetchTwoPartJokeByKey(TwoPartJoke joke);
        void didFetchAllPosts(List<Post> posts);
    }

    public void setListener(TestRepositoryListener listener) {
        this.listener = listener;
    }

    public void fetchAllPosts() {
        Call<List<Post>> call = postApi.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    listener.didFetchError("Response Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();
                listener.didFetchAllPosts(posts);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                listener.didFetchError(t.getMessage());
            }
        });
    }

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

    public void fetchTwoPartJoke() {
        Call<TwoPartJoke> call = jokeApi.getTwoPartJoke();
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
        Call<SingleJoke> call = jokeApi.getSingleJokeBy(key);
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
        Call<TwoPartJoke> call = jokeApi.getTwoPartJokeBy(key);
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
