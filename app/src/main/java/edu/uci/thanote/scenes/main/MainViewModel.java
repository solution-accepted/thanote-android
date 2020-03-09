package edu.uci.thanote.scenes.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.recipepuppy.Recipe;
import edu.uci.thanote.apis.thecocktaildb.Cocktail;
import edu.uci.thanote.apis.themoviedb.Movie;
import edu.uci.thanote.helpers.SharePreferencesHelper;
import edu.uci.thanote.apis.Api;

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

    private MainRepository.MainRepositoryListener repositoryListener = new MainRepository.MainRepositoryListener() {
        @Override
        public void didFetchError(String message) {
            listener.didFetchError(message);
        }

        @Override
        public void didFetchSingleJoke(SingleJoke joke) {
            SharePreferencesHelper.getInstance(application).setTitle(Api.JOKE.toString());
            SharePreferencesHelper.getInstance(application).setMessage(joke.getJoke());
        }

        @Override
        public void didFetchRecipe(Recipe recipe) {
            SharePreferencesHelper.getInstance(application).setTitle(Api.RECIPEPUPPY.toString());
            // TODO: - Junxian! Please help finish the content(message) for notification below
            SharePreferencesHelper.getInstance(application).setMessage("test data for a recipe");
        }

        @Override
        public void didFetchMovie(Movie movie) {
            SharePreferencesHelper.getInstance(application).setTitle(Api.THEMOVIEDB.toString());
            // TODO: - Junxian! Please help finish the content(message) for notification below
            SharePreferencesHelper.getInstance(application).setMessage("test data for a movie");
        }

        @Override
        public void didFetchCocktail(Cocktail cocktail) {
            SharePreferencesHelper.getInstance(application).setTitle(Api.THECOCKTAILDB.toString());
            // TODO: - Junxian! Please help finish the content(message) for notification below
            SharePreferencesHelper.getInstance(application).setMessage("test data for a cocktail");
        }
    };

    public interface MainViewModelListener {
        void didFetchError(String message);
    }

    public void setListener(MainViewModelListener listener) {
        this.listener = listener;
    }

    public void updateNotificationContent() {
        String category = SharePreferencesHelper.getInstance(application).getCategory();

        if (category.equals(Api.JOKE.toString())) {
            repository.fetchSingleJoke();
        } else if (category.equals(Api.RECIPEPUPPY.toString())) {
            repository.fetchRecipe();
        } else if (category.equals(Api.THEMOVIEDB.toString())) {
            repository.fetchMovie();
        } else if (category.equals(Api.THECOCKTAILDB.toString())) {
            repository.fetchCocktail();
        } else {
            repository.fetchSingleJoke();
        }
    }
}
