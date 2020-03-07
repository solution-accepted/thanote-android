package edu.uci.thanote.scenes.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.helpers.SharePreferencesHelper;

public class MainViewModel extends AndroidViewModel {
    private MainViewModelListener listener;
    private final MainRepository repository;
    private Application application;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MainRepository(application);
        repository.setListener(repositoryListener);
        this.application = application;
    }

    // TODO: add more listener from MainRepositoryListener
    private MainRepository.MainRepositoryListener repositoryListener = new MainRepository.MainRepositoryListener() {
        @Override
        public void didFetchError(String message) {
            listener.didFetchError(message);
        }

        @Override
        public void didFetchSingleJoke(SingleJoke joke) {
            SharePreferencesHelper.getInstance(application).setTitle("Joke");
            SharePreferencesHelper.getInstance(application).setMessage(joke.getJoke());
            listener.didFetchError(joke.getJoke());
        }
    };

    public interface MainViewModelListener {
        void didFetchError(String message);
    }

    public void setListener(MainViewModelListener listener) {
        this.listener = listener;
    }

    public void updateNotificationContent() {
        // TODO: add more api calls
        String category = SharePreferencesHelper.getInstance(application).getCategory();

        // TODO: create a api enum to manage
        if (category.equals("Joke")) {
            repository.fetchSingleJoke();
        }
    }
}
