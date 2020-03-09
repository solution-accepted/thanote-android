package edu.uci.thanote.scenes.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import edu.uci.thanote.apis.Api;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.apis.thecocktaildb.CocktailResponse;
import edu.uci.thanote.apis.themoviedb.TMDbMoviesResponse;
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

    private MainRepository.MainRepositoryListener repositoryListener = new MainRepository.MainRepositoryListener() {

        @Override
        public void didFetchError(String message) {
            listener.didFetchError(message);
        }

        @Override
        public void didFetchSingleJokeRandomly(SingleJoke joke) {
            final SharePreferencesHelper helper = SharePreferencesHelper.getInstance(application);
            helper.setTitle(Api.JOKE.toString());
            helper.setMessage(joke.getJoke());
        }

        @Override
        public void didFetchPuppyRecipesRandomly(RecipePuppyResponse recipes) {
            final SharePreferencesHelper helper = SharePreferencesHelper.getInstance(application);
            helper.setTitle(Api.RECIPEPUPPY.toString());
            helper.setMessage(recipes.getRecipes().get(0).getTitle());
        }

        @Override
        public void didFetchTMDBMovieRandomly(TMDbMoviesResponse movies) {
            final SharePreferencesHelper helper = SharePreferencesHelper.getInstance(application);
            helper.setTitle(Api.THEMOVIEDB.toString());
            helper.setMessage(movies.getMovies().get(0).getTitle());
        }

        @Override
        public void didFetchCocktailRandomly(CocktailResponse cocktail) {
            final SharePreferencesHelper helper = SharePreferencesHelper.getInstance(application);
            helper.setTitle(Api.THECOCKTAILDB.toString());
            helper.setMessage(cocktail.getCocktails().get(0).getName());
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
            repository.fetchSingleJokeRandomly();
        } else if (category.equals(Api.RECIPEPUPPY.toString())) {
            repository.fetchPuppyRecipesRandomly();
        } else if (category.equals(Api.THEMOVIEDB.toString())) {
            repository.fetchTMDBMovieRandomly();
        } else if (category.equals(Api.THECOCKTAILDB.toString())) {
            repository.fetchCocktailRandomly();
        } else {
            repository.fetchSingleJokeRandomly();
        }
    }
}
