package edu.uci.thanote.scenes.main.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import edu.uci.thanote.R;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.omdb.OMDbMovie;
import edu.uci.thanote.apis.omdb.OMDbMovieSearchResponse;
import edu.uci.thanote.apis.recipepuppy.Recipe;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.apis.thecocktaildb.Cocktail;
import edu.uci.thanote.apis.thecocktaildb.CocktailResponse;
import edu.uci.thanote.apis.themoviedb.TMDbMovie;
import edu.uci.thanote.apis.themoviedb.TMDbMoviesResponse;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.note.Note;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("FieldCanBeLocal")
public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";

    // region API constants

    private final int NOTE_RANDOM_DISPLAY_COUNT = 10;
    private final int NOTE_RANDOM_TYPE_COUNT = 5;

    private final int NOTE_DEFAULT_CATEGORY_ID = Category.DEFAULT_CATEGORY_ID;
    private final String NOTE_DEFAULT_IMAGE_URL = "";

    private final String NOTE_JOKE_TITLE_PREFIX = "[Joke]: ";

    private final String NOTE_RECIPE_TITLE_PREFIX = "[Recipe]: ";
    private final int NOTE_RECIPE_COUNT_IN_RESPONSE = 10;

    private final String NOTE_MOVIE_TITLE_PREFIX = "[Movie]: ";
    private final int NOTE_TMDB_MOVIE_COUNT_IN_RESPONSE = 20;

    private final String NOTE_COCKTAIL_TITLE_PREFIX = "[Cocktail]: ";
    private final int NOTE_COCKTAIL_COUNT_IN_RANDOM_RESPONSE = 1;
    private final int NOTE_COCKTAIL_COUNT_IN_SEARCH_RESPONSE = 25;

    private enum API {
        ALL,
        JOKE,
        RECIPE,
        MOVIE,
        COCKTAIL
    }

    private API apiSelected = API.ALL;

    // endregion

    // region view model

    private HomeViewModel viewModel;
    private List<Category> categoryList;

    // endregion

    // region UI components

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageViewGalaxy;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private HomeRecyclerViewAdapter recyclerViewAdapter;
    private Spinner spinnerApiSwitch;

    // endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViewModel();
        setupViews(view);
        fetchSomeRandomNotes();
        return view;
    }

    private final HomeViewModel.Listener vmListener = new HomeViewModel.Listener() {
        @Override
        public void didFetchError(String message) {
            showToast(message);
        }

        @Override
        public void didVerifyError(String message) {
            showToast(message);
        }

        @Override
        public void didFetchSingleJokeRandomly(SingleJoke joke) {
            swipeRefreshLayout.setRefreshing(false);
            if (joke.isError()) {
                return;
            }
            viewModel.insertNoteIntoMemory(new Note(
                    NOTE_JOKE_TITLE_PREFIX + joke.getCategory(),
                    joke.getJoke(),
                    NOTE_DEFAULT_CATEGORY_ID,
                    NOTE_DEFAULT_IMAGE_URL));

        }

        @Override
        public void didFetchTwoPartJokeRandomly(TwoPartJoke joke) {
            swipeRefreshLayout.setRefreshing(false);
            if (joke.isError()) {
                return;
            }
            viewModel.insertNoteIntoMemory(new Note(
                    NOTE_JOKE_TITLE_PREFIX + joke.getCategory(),
                    joke.getSetup() + "\n" + joke.getDelivery(),
                    NOTE_DEFAULT_CATEGORY_ID,
                    NOTE_DEFAULT_IMAGE_URL));
        }

        @Override
        public void didFetchSingleJokeByKey(SingleJoke joke) {
            didFetchSingleJokeRandomly(joke);
        }

        @Override
        public void didFetchTwoPartJokeByKey(TwoPartJoke joke) {
            didFetchTwoPartJokeRandomly(joke);
        }

        @Override
        public void didFetchPuppyRecipesRandomly(RecipePuppyResponse recipes) {
            swipeRefreshLayout.setRefreshing(false);
            List<Recipe> recipeList = recipes.getRecipes();
            if (recipeList.isEmpty()) {
                return;
            }
            final int next = new Random().nextInt(NOTE_RECIPE_COUNT_IN_RESPONSE);
            Recipe recipe = recipeList.get(next);
            Note note = new Note(
                    NOTE_RECIPE_TITLE_PREFIX + recipe.getTitle(),
                    recipe.getIngredients() + "\n" + recipe.getWebsiteUrl(),
                    NOTE_DEFAULT_CATEGORY_ID,
                    recipe.getThumbnail()
            );
            viewModel.insertNoteIntoMemory(note);
        }

        @Override
        public void didFetchPuppyRecipesByParams(RecipePuppyResponse recipes) {
            swipeRefreshLayout.setRefreshing(false);
            if (recipes.getRecipes().isEmpty()) {
                return;
            }
            switch (apiSelected) {
                case ALL:
                    didFetchPuppyRecipesRandomly(recipes);
                    break;
                case RECIPE:
                    recipes.getRecipes().forEach(recipe -> viewModel.insertNoteIntoMemory(new Note(
                            NOTE_RECIPE_TITLE_PREFIX + recipe.getTitle(),
                            recipe.getIngredients() + "\n" + recipe.getWebsiteUrl(),
                            NOTE_DEFAULT_CATEGORY_ID,
                            recipe.getThumbnail()
                    )));
                    break;
                default:
                    Log.e(TAG, "didFetchPuppyRecipesByParams: apiSelected = " + apiSelected);
            }
        }

        @Override
        public void didFetchOMDBMovie(OMDbMovie movie) {
            swipeRefreshLayout.setRefreshing(false);
            if (movie.getResponse().equals("False")) {
                return;
            }
            switch (apiSelected) {
                case ALL:
                    viewModel.insertNoteIntoMemory(new Note(
                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                            movie.getPlot() + "\n" + movie.getImdbUrl(),
                            NOTE_DEFAULT_CATEGORY_ID,
                            movie.getImageUrl()));
                    break;
                case MOVIE:
                    break;
                default:
                    Log.e(TAG, "didFetchOMDBMovie: apiSelected = " + apiSelected);
            }
        }

        @Override
        public void didFetchOMDBMovieSearch(OMDbMovieSearchResponse movies) {
            swipeRefreshLayout.setRefreshing(false);
            if (movies.getResponse().equals("False")) {
                return;
            }
            switch (apiSelected) {
                case ALL:
                    break;
                case MOVIE:
                    movies.getResults().forEach(movie -> viewModel.insertNoteIntoMemory(new Note(
                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                            movie.getImdbUrl(),
                            NOTE_DEFAULT_CATEGORY_ID,
                            movie.getImageUrl())));
                    break;
                default:
                    Log.e(TAG, "didFetchOMDBMovieSearch: apiSelected = " + apiSelected);
            }
        }

        @Override
        public void didFetchTMDBMovieRandomly(TMDbMoviesResponse movies) {
            swipeRefreshLayout.setRefreshing(false);
            if (movies.getMovies() == null) {
                return;
            }
            TMDbMovie movie = movies.getMovies().get(new Random().nextInt(NOTE_TMDB_MOVIE_COUNT_IN_RESPONSE));
            switch (apiSelected) {
                case ALL:
                    viewModel.insertNoteIntoMemory(new Note(
                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                            movie.getOverview(),
                            NOTE_DEFAULT_CATEGORY_ID,
                            movie.getImageUrl()));
                    break;
                case MOVIE:
                    break;
                default:
                    Log.e(TAG, "didFetchTMDBMovieRandomly: apiSelected = " + apiSelected);
            }
        }

        @Override
        public void didFetchTMDBMovieBySearching(TMDbMoviesResponse movies) {
            swipeRefreshLayout.setRefreshing(false);
            List<TMDbMovie> movieList = movies.getMovies();
            if (movieList == null || movieList.isEmpty()) {
                return;
            }
            switch (apiSelected) {
                case ALL: {
                    TMDbMovie movie = movieList.get(0);
                    viewModel.insertNoteIntoMemory(new Note(
                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                            movie.getOverview(),
                            NOTE_DEFAULT_CATEGORY_ID,
                            movie.getImageUrl()));
                }
                break;
                case MOVIE: {
                    movies.getMovies().forEach(movie -> viewModel.insertNoteIntoMemory(new Note(
                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                            movie.getOverview(),
                            NOTE_DEFAULT_CATEGORY_ID,
                            movie.getImageUrl())));
                }
                break;
                default:
                    Log.e(TAG, "didFetchTMDBMovieBySearching: apiSelected = " + apiSelected);
            }
        }

        @Override
        public void didFetchCocktailRandomly(CocktailResponse cocktails) {
            swipeRefreshLayout.setRefreshing(false);
            List<Cocktail> cocktailList = cocktails.getCocktails();
            if (cocktailList == null || cocktailList.isEmpty()) {
                return;
            }
            Cocktail cocktail = cocktailList.get(0);
            viewModel.insertNoteIntoMemory(new Note(
                    NOTE_COCKTAIL_TITLE_PREFIX + cocktail.getName(),
                    cocktail.getInstruction(),
                    NOTE_DEFAULT_CATEGORY_ID,
                    cocktail.getImageUrl()));

        }

        @Override
        public void didFetchCocktailBySearching(CocktailResponse cocktails) {
            swipeRefreshLayout.setRefreshing(false);
            List<Cocktail> cocktailList = cocktails.getCocktails();
            if (cocktailList == null || cocktailList.isEmpty()) {
                return;
            }
            switch (apiSelected) {
                case ALL: {
                    Cocktail cocktail = cocktailList.get(new Random().nextInt(cocktailList.size()));
                    viewModel.insertNoteIntoMemory(new Note(
                            NOTE_COCKTAIL_TITLE_PREFIX + cocktail.getName(),
                            cocktail.getInstruction(),
                            NOTE_DEFAULT_CATEGORY_ID,
                            cocktail.getImageUrl()));
                }
                break;
                case COCKTAIL:
                    cocktailList.forEach(cocktail -> viewModel.insertNoteIntoMemory(new Note(
                            NOTE_COCKTAIL_TITLE_PREFIX + cocktail.getName(),
                            cocktail.getInstruction(),
                            NOTE_DEFAULT_CATEGORY_ID,
                            cocktail.getImageUrl())));
                    break;
                default:
                    Log.e(TAG, "didFetchCocktailBySearching: apiSelected = " + apiSelected);
            }
        }
    };

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.setListener(vmListener);
        viewModel.getNotesInMemory().observe(getViewLifecycleOwner(),
                notes -> recyclerViewAdapter.setNotes(notes));
        viewModel.getCategoriesInDatabase().observe(getViewLifecycleOwner(),
                categories -> categoryList = categories);
        viewModel.getNotesInDatabase().observe(getViewLifecycleOwner(),
                notes -> Log.i(TAG, "setupViewModel: notes in database = "
                        + Arrays.toString(notes.stream().map(Note::getTitle).toArray(String[]::new))));
    }

    private void setupViews(View view) {
        // region swipeRefreshLayout

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_home);
        swipeRefreshLayout.setOnRefreshListener(this::fetchSomeRandomNotes);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // endregion

        // region imageView (Galaxy)

        imageViewGalaxy = view.findViewById(R.id.image_view_home_galaxy);
        Glide.with(view).load(R.drawable.home_galaxy).into(imageViewGalaxy);

        // endregion

        // region searchView

        searchView = view.findViewById(R.id.search_view_home);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(getString(R.string.home_search_hint));

        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());

        // Also try:
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "searchView.onQueryTextSubmit: apiSelected = " + apiSelected);
                Log.i(TAG, "searchView.onQueryTextSubmit: query = " + query);
                if (query.isEmpty()) {
                    fetchSomeRandomNotes();
                } else {
                    searchNote(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "searchView.onQueryTextChange: newText = " + newText);
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) ->
                Log.i(TAG, "setOnQueryTextFocusChangeListener: " + hasFocus));

        // Triggered when the search icon is clicked:
//        searchView.setOnSearchClickListener(v -> { });

        // endregion

        // region recyclerView

        recyclerViewAdapter = new HomeRecyclerViewAdapter();
        recyclerViewAdapter.setListener(new HomeRecyclerViewAdapter.Listener() {
            @Override
            public void onButtonShareClicked(Note note) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, note.getTitle() + "\n" + note.getDetail());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }

            @Override
            public void onButtonFavoriteClicked(Note note, ImageButton buttonFavorite) {
                final String[] categoryStrings = categoryList.stream().map(Category::getName).toArray(String[]::new);
                AtomicInteger categorySelected = new AtomicInteger(0);
                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle("Choose a category")
                        .setSingleChoiceItems(categoryStrings, categorySelected.get(), (dialog, which) -> {
                            Log.i(TAG, "onButtonFavoriteClicked: which = " + which);
                            categorySelected.set(which);
                        })
                        .setPositiveButton("OK", (dialog, which) -> {
                            Log.i(TAG, "onButtonFavoriteClicked: categorySelected = " + categorySelected.get());
                            note.setCategoryId(categoryList.get(categorySelected.get()).getId());
                            viewModel.insertNoteIntoDatabase(note);
                            buttonFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                            buttonFavorite.setEnabled(false);
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        // endregion

        // region apiSwitch
        spinnerApiSwitch = view.findViewById(R.id.spinner_home_api_switch);
        spinnerApiSwitch.setAdapter(new ArrayAdapter<>(
                Objects.requireNonNull(getContext()),
                R.layout.support_simple_spinner_dropdown_item,
                Arrays.stream(API.values())
                        .map(Enum::name)
                        .map(allCapitalized -> allCapitalized.substring(0, 1).toUpperCase()
                                + allCapitalized.substring(1).toLowerCase())
                        .toArray(String[]::new)
        ));
        spinnerApiSwitch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: position = " + position);
                Log.i(TAG, "onItemSelected: id = " + id);
                apiSelected = API.values()[position];
                Log.i(TAG, "onItemSelected: apiSelected = " + apiSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });
        // endregion
    }

    private void fetchSomeRandomNotes() {
        viewModel.deleteNotesFromMemory();
        for (int i = 0; i < NOTE_RANDOM_DISPLAY_COUNT; ++i) {
            fetchSingleRandomNote();
        }
    }

    private void fetchSingleRandomNote() {
        int next = new Random().nextInt(NOTE_RANDOM_TYPE_COUNT);
        switch (next) {
            case 0:
                viewModel.fetchSingleJoke();
                break;
            case 1:
                viewModel.fetchTwoPartJoke();
                break;
            case 2:
                viewModel.fetchPuppyRecipesRandomly();
                break;
            case 3:
                viewModel.fetchTMDBMovieRandomly();
                break;
            case 4:
                viewModel.fetchCocktailRandomly();
                break;
            default:
                Log.e(TAG, "getSingleRandomNote: unknown next id = " + next);
        }
    }

    private void searchNote(String query) {
        swipeRefreshLayout.setRefreshing(true);
        viewModel.deleteNotesFromMemory();
        switch (apiSelected) {
            case ALL:
                searchJoke(query);
                searchRecipe(query);
                searchMovie(query);
                searchCocktail(query);
                break;
            case JOKE:
                searchJoke(query);
                break;
            case RECIPE:
                searchRecipe(query);
                break;
            case MOVIE:
                searchMovie(query);
                break;
            case COCKTAIL:
                searchCocktail(query);
                break;
            default:
                Log.e(TAG, "searchNote: unknown apiSelected =" + apiSelected);
        }

    }

    private void searchJoke(String query) {
        viewModel.searchSingleJoke(query);
        viewModel.searchTwoPartJoke(query);
    }

    private void searchRecipe(String query) {
        viewModel.searchPuppyRecipes(query);
    }

    private void searchMovie(String query) {
        viewModel.searchOMDBMovie(query);
        viewModel.searchTMDBMovie(query);
    }

    private void searchCocktail(String query) {
        viewModel.searchCocktail(query);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
