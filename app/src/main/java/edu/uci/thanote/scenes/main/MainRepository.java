package edu.uci.thanote.scenes.main;

import android.app.Application;
import edu.uci.thanote.apis.APIClient;
import edu.uci.thanote.apis.joke.JokeApi;
import edu.uci.thanote.apis.joke.SingleJoke;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainRepository {
    private MainRepositoryListener listener;

    // api
    private JokeApi jokeApi;
    // TODO: - add fetch other apis

    public MainRepository(Application application) {
        // initial retrofit
        Retrofit jokeRetrofit = APIClient.getInstance().getRetrofitJoke();
        jokeApi = jokeRetrofit.create(JokeApi.class);

        // TODO: - add fetch other apis
    }

    // region Public APIs (API)

    public interface MainRepositoryListener {
        void didFetchError(String message);
        void didFetchSingleJoke(SingleJoke joke);
        // TODO: - add fetch other apis
    }

    public void setListener(MainRepositoryListener listener) {
        this.listener = listener;
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

    // TODO: - add fetch other apis

    // endregion
}
