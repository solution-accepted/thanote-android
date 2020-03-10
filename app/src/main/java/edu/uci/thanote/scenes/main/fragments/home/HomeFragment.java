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
import edu.uci.thanote.apis.nasa.NasaApod;
import edu.uci.thanote.apis.numbers.Number;
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

    private final int NOTE_RANDOM_DISPLAY_COUNT = 10; // Try 100, super bad performance

    private final String NOTE_JOKE_TITLE_PREFIX = "[Joke] ";

    private final String NOTE_RECIPE_TITLE_PREFIX = "[Recipe] ";
    private final int NOTE_RECIPE_COUNT_IN_RESPONSE = 10;

    private final String NOTE_MOVIE_TITLE_PREFIX = "[Movie] ";
    private final int NOTE_TMDB_MOVIE_COUNT_IN_RESPONSE = 20;

    private final String NOTE_COCKTAIL_TITLE_PREFIX = "[Cocktail] ";
    private final int NOTE_COCKTAIL_COUNT_IN_RANDOM_RESPONSE = 1;
    private final int NOTE_COCKTAIL_COUNT_IN_SEARCH_RESPONSE = 25;

    private final String NOTE_NASA_TITLE_PREFIX = "[Nasa] ";
    private final String NOTE_NUMBER_TITLE_PREFIX = "[Number] ";

    private enum API {
        ALL,
        JOKE,
        RECIPE,
        MOVIE,
        COCKTAIL,
        NASA,
        NUMBER
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
            swipeRefreshLayout.setRefreshing(false);
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
            viewModel.insertNoteIntoMemory(
                    NOTE_JOKE_TITLE_PREFIX + joke.getCategory(),
                    joke.getJoke()
            );

        }

        @Override
        public void didFetchTwoPartJokeRandomly(TwoPartJoke joke) {
            swipeRefreshLayout.setRefreshing(false);
            if (joke.isError()) {
                return;
            }
            viewModel.insertNoteIntoMemory(
                    NOTE_JOKE_TITLE_PREFIX + joke.getCategory(),
                    joke.getSetup() + "\n" + joke.getDelivery()
            );
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
            final List<Recipe> recipeList = recipes.getRecipes();
            if (recipeList == null || recipeList.isEmpty()) {
                return;
            }
            Recipe recipe = recipeList.get(new Random().nextInt(NOTE_RECIPE_COUNT_IN_RESPONSE));
            viewModel.insertNoteIntoMemory(
                    NOTE_RECIPE_TITLE_PREFIX + recipe.getTitle(),
                    recipe.getIngredients() + "\n" + recipe.getWebsiteUrl(),
                    recipe.getThumbnail()
            );
        }

        @Override
        public void didFetchPuppyRecipesByParams(RecipePuppyResponse recipes) {
            swipeRefreshLayout.setRefreshing(false);
            final List<Recipe> recipeList = recipes.getRecipes();
            if (recipeList == null || recipeList.isEmpty()) {
                return;
            }
            switch (apiSelected) {
                case ALL:
                    didFetchPuppyRecipesRandomly(recipes);
                    break;
                case RECIPE:
                    recipeList.forEach(recipe ->
                            viewModel.insertNoteIntoMemory(
                                    NOTE_RECIPE_TITLE_PREFIX + recipe.getTitle(),
                                    recipe.getIngredients() + "\n" + recipe.getWebsiteUrl(),
                                    recipe.getThumbnail()
                            )
                    );
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
                    viewModel.insertNoteIntoMemory(
                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                            movie.getPlot() + "\n" + movie.getImdbUrl(),
                            movie.getImageUrl()
                    );
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
                    movies.getResults()
                            .forEach(movie ->
                                    viewModel.insertNoteIntoMemory(
                                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                                            movie.getImdbUrl(),
                                            movie.getImageUrl()
                                    )
                            );
                    break;
                default:
                    Log.e(TAG, "didFetchOMDBMovieSearch: apiSelected = " + apiSelected);
            }
        }

        @Override
        public void didFetchTMDBMovieRandomly(TMDbMoviesResponse movies) {
            swipeRefreshLayout.setRefreshing(false);
            final List<TMDbMovie> movieList = movies.getMovies();
            if (movieList == null || movieList.isEmpty()) {
                return;
            }
            TMDbMovie movie = movieList.get(new Random().nextInt(NOTE_TMDB_MOVIE_COUNT_IN_RESPONSE));
            switch (apiSelected) {
                case ALL:
                    viewModel.insertNoteIntoMemory(
                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                            movie.getOverview(),
                            movie.getImageUrl()
                    );
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
            final List<TMDbMovie> movieList = movies.getMovies();
            if (movieList == null || movieList.isEmpty()) {
                return;
            }
            switch (apiSelected) {
                case ALL: {
                    TMDbMovie movie = movieList.get(0);
                    viewModel.insertNoteIntoMemory(
                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                            movie.getOverview(),
                            movie.getImageUrl()
                    );
                }
                break;
                case MOVIE:
                    movieList.forEach(movie -> viewModel.insertNoteIntoMemory(
                            NOTE_MOVIE_TITLE_PREFIX + movie.getTitle(),
                            movie.getOverview(),
                            movie.getImageUrl()
                            )
                    );
                    break;
                default:
                    Log.e(TAG, "didFetchTMDBMovieBySearching: apiSelected = " + apiSelected);
            }
        }

        @Override
        public void didFetchCocktailRandomly(CocktailResponse cocktails) {
            swipeRefreshLayout.setRefreshing(false);
            final List<Cocktail> cocktailList = cocktails.getCocktails();
            if (cocktailList == null || cocktailList.isEmpty()) {
                return;
            }
            Cocktail cocktail = cocktailList.get(0);
            viewModel.insertNoteIntoMemory(
                    NOTE_COCKTAIL_TITLE_PREFIX + cocktail.getName(),
                    cocktail.getInstruction(),
                    cocktail.getImageUrl()
            );
        }

        @Override
        public void didFetchCocktailBySearching(CocktailResponse cocktails) {
            swipeRefreshLayout.setRefreshing(false);
            final List<Cocktail> cocktailList = cocktails.getCocktails();
            if (cocktailList == null || cocktailList.isEmpty()) {
                return;
            }
            switch (apiSelected) {
                case ALL: {
                    Cocktail cocktail = cocktailList.get(new Random().nextInt(cocktailList.size()));
                    viewModel.insertNoteIntoMemory(
                            NOTE_COCKTAIL_TITLE_PREFIX + cocktail.getName(),
                            cocktail.getInstruction(),
                            cocktail.getImageUrl()
                    );
                }
                break;
                case COCKTAIL:
                    cocktailList.forEach(cocktail -> viewModel.insertNoteIntoMemory(
                            NOTE_COCKTAIL_TITLE_PREFIX + cocktail.getName(),
                            cocktail.getInstruction(),
                            cocktail.getImageUrl())
                    );
                    break;
                default:
                    Log.e(TAG, "didFetchCocktailBySearching: apiSelected = " + apiSelected);
            }
        }

        @Override
        public void didFetchNasaApodRandomly(NasaApod nasaApod) {
            swipeRefreshLayout.setRefreshing(false);
            final String url = nasaApod.getImageUrl();
            if (nasaApod.getCode() != null || url == null || url.isEmpty()) {
                return;
            }
            viewModel.insertNoteIntoMemory(
                    NOTE_NASA_TITLE_PREFIX + nasaApod.getTitle(),
                    nasaApod.getDetail(),
                    nasaApod.getImageUrl()
            );
        }

        @Override
        public void didFetchNumber(Number number) {
            swipeRefreshLayout.setRefreshing(false);
            if (!number.isFound()) {
                return;
            }
            viewModel.insertNoteIntoMemory(
                    NOTE_NUMBER_TITLE_PREFIX + number.getTitle(),
                    number.getDetail()
            );
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

        //
        // Make the whole searchView clickable
        //
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());
        // Also try:
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(false);

        //
        // Get the reference for the close button
        //
        // androidx.appcompat.R.id.search_close_btn
        // android.support.v7.appcompat.R.id.search_close_btn
        // android:id/search_close_btn
        final int searchViewCloseButtonId = searchView.getResources().getIdentifier("android:id/search_close_btn", null, null);
        final View searchViewCloseButton = searchView.findViewById(searchViewCloseButtonId);
        searchViewCloseButton.setOnClickListener(v -> searchView.onActionViewCollapsed());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "searchView.onQueryTextSubmit: apiSelected = " + apiSelected);
                Log.i(TAG, "searchView.onQueryTextSubmit: query = " + query);
                if (!query.isEmpty()) {
                    searchNote(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "searchView.onQueryTextChange: newText = " + newText);
                if (newText.isEmpty()) {
                    viewModel.restoreNotesInMemory();
                    return true;
                }
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
        viewModel.backupNotesInMemory();
    }

    private void fetchSingleRandomNote() {
        API next = API.values()[new Random().nextInt(API.values().length)];
        switch (next) {
            case ALL:
                // Er... bad luck
                fetchSingleRandomNote(); // Do it again!
                // Hope it will not always be ALL
                // The worst situation: next == ALL every time and stack overflow
                // Is it possible?
                break;
            case JOKE:
                viewModel.fetchSingleJoke();
                break;
            case RECIPE:
                viewModel.fetchPuppyRecipesRandomly();
                break;
            case MOVIE:
                viewModel.fetchTMDBMovieRandomly();
                break;
            case COCKTAIL:
                viewModel.fetchCocktailRandomly();
                break;
            case NASA:
                viewModel.fetchNasaApodRandomly();
                break;
            case NUMBER:
                viewModel.fetchNumberRandomly();
                break;
            default:
                Log.e(TAG, "getSingleRandomNote: unknown next API = " + next);
        }
    }

    private void searchNote(String query) {
        swipeRefreshLayout.setRefreshing(true);
        viewModel.deleteNotesFromMemory();
        switch (apiSelected) {
            case ALL:
                searchJoke(query);
                viewModel.searchPuppyRecipes(query);
                searchMovie(query);
                viewModel.searchCocktail(query);
                viewModel.searchNasaApod(query);
                viewModel.searchNumber(query);
                break;
            case JOKE:
                searchJoke(query);
                break;
            case RECIPE:
                viewModel.searchPuppyRecipes(query);
                break;
            case MOVIE:
                searchMovie(query);
                break;
            case COCKTAIL:
                viewModel.searchCocktail(query);
                break;
            case NASA:
                viewModel.searchNasaApod(query);
                break;
            case NUMBER:
                viewModel.searchNumber(query);
                break;
            default:
                Log.e(TAG, "searchNote: unknown apiSelected =" + apiSelected);
        }

    }

    private void searchJoke(String query) {
        viewModel.searchSingleJoke(query);
        viewModel.searchTwoPartJoke(query);
    }

    private void searchMovie(String query) {
        viewModel.searchOMDBMovie(query);
        viewModel.searchTMDBMovie(query);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
