package edu.uci.thanote.scenes.main.fragments.home;

import android.content.Intent;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import edu.uci.thanote.R;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.apis.recipepuppy.Recipe;
import edu.uci.thanote.apis.recipepuppy.RecipePuppyResponse;
import edu.uci.thanote.databases.note.Note;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";

    private final int NOTE_INIT_COUNT = 10;
    private final int NOTE_TYPE_COUNT = 3;
    private final int NOTE_DEFAULT_CATEGORY_ID = 1;
    private final String NOTE_DEFAULT_IMAGE_URL = "";


    private final String NOTE_RECIPE_TITLE = "Recipe: ";
    private final int NOTE_RECIPE_COUNT_IN_RESPONSE = 10;

    private enum API {
        ALL,
        JOKE,
        RECIPE
    }

    private API apiSelected = API.ALL;

    private HomeViewModel viewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private HomeRecyclerViewAdapter recyclerViewAdapter;
    private Spinner spinnerApiSwitch;

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
        public void didFetchSingleJokeRandomly(SingleJoke joke) {
            swipeRefreshLayout.setRefreshing(false);
            if (joke.isError()) {
                return;
            }
            viewModel.insertNoteIntoMemory(new Note(
                    joke.getCategory() + " Joke",
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
                    joke.getCategory() + " Joke",
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
                    NOTE_RECIPE_TITLE + recipe.getTitle(),
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
                            NOTE_RECIPE_TITLE + recipe.getTitle(),
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
        public void didFetchError(String message) {
            showToast(message);
        }

        @Override
        public void didVerifyError(String message) {
            showToast(message);
        }
    };

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.setListener(vmListener);
        viewModel.getNotesInMemory().observe(getViewLifecycleOwner(),
                notes -> recyclerViewAdapter.setNotes(notes));
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

        imageView = view.findViewById(R.id.image_view_home);
        Drawable decodedAnimation = null;
        try {
            decodedAnimation = ImageDecoder.decodeDrawable(
                    ImageDecoder.createSource(getResources(), R.drawable.home_galaxy)
            );
        } catch (IOException e) {
            Log.e(TAG, "setupViews: Failed to ImageDecoder.decodeDrawable", e);
        }
        imageView.setImageDrawable(decodedAnimation);
        ((AnimatedImageDrawable) Objects.requireNonNull(decodedAnimation)).start();

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
                searchNote(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "searchView.onQueryTextChange: newText = " + newText);
                if (newText.isEmpty()) {
                    viewModel.restoreNotesInMemory();
                    return true;
                }
                return false;
//                return onQueryTextSubmit(newText);
            }
        });

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
                sendIntent.putExtra(Intent.EXTRA_TEXT, note.getDetail());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }

            @Override
            public void onButtonFavoriteClicked(Note note) {
                // TODO note.isFavorite()
//                if (note.isFavorite()) {
//                    viewModel.deleteNote(note);
//                } else {
//                    viewModel.insertNote(note);
//                }
                showToast("onButtonFavoriteClicked");
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
        for (int i = 0; i < NOTE_INIT_COUNT; ++i) {
            Log.i(TAG, "getSomeRandomNotes: fetching note " + i);
            fetchSingleRandomNote();
        }
        viewModel.backupNotesInMemory();
    }

    private void fetchSingleRandomNote() {
        int next = new Random().nextInt(NOTE_TYPE_COUNT);
        switch (next) {
            case 0:
                viewModel.fetchSingleJokeFromApi();
                break;
            case 1:
                viewModel.fetchTwoPartJokeFromApi();
                break;
            case 2:
                viewModel.fetchPuppyRecipesFromApiRandomly();
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
                break;
            case JOKE:
                searchJoke(query);
                break;
            case RECIPE:
                searchRecipe(query);
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

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
