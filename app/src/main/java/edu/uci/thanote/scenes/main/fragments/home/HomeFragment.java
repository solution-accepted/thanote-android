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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
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
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";

    private final int NOTE_INIT_COUNT = 10;
    private final int NOTE_TYPE_COUNT = 3;
    private final int NOTE_DEFAULT_CATEGORY_ID = 1;
    private final String NOTE_JOKE_TITLE = "Joke";
    private final String NOTE_RECIPE_TITLE = "Recipe: ";
    private final String NOTE_DEFAULT_IMAGE_URL = "";
    private final int NOTE_RECIPE_COUNT_IN_RESPONSE = 10;

    private HomeViewModel viewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private HomeRecyclerViewAdapter recyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViewModel();
        setupViews(view);
        getSomeRandomNotes();
        return view;
    }

    private final HomeViewModel.Listener vmListener = new HomeViewModel.Listener() {
        @Override
        public void didFetchSingleJoke(SingleJoke joke) {
            swipeRefreshLayout.setRefreshing(false);
            String noteDetail = joke.getJoke();
            Note note = new Note(
                    NOTE_JOKE_TITLE,
                    noteDetail,
                    NOTE_DEFAULT_CATEGORY_ID,
                    NOTE_DEFAULT_IMAGE_URL);
            viewModel.insertNoteInMemory(note);
        }

        @Override
        public void didFetchTwoPartJoke(TwoPartJoke joke) {
            swipeRefreshLayout.setRefreshing(false);
            String noteDetail = joke.getSetup() + "\n" + joke.getDelivery();
            Note note = new Note(
                    NOTE_JOKE_TITLE,
                    noteDetail,
                    NOTE_DEFAULT_CATEGORY_ID,
                    NOTE_DEFAULT_IMAGE_URL);
            viewModel.insertNoteInMemory(note);
        }

        @Override
        public void didFetchSingleJokeByKey(SingleJoke joke) {
            viewModel.deleteNotesInMemory();
            didFetchSingleJoke(joke);
        }

        @Override
        public void didFetchTwoPartJokeByKey(TwoPartJoke joke) {
            viewModel.deleteNotesInMemory();
            didFetchTwoPartJoke(joke);
        }

        @Override
        public void didFetchPuppyRecipes(RecipePuppyResponse recipes) {
            swipeRefreshLayout.setRefreshing(false);
            List<Recipe> recipeList = recipes.getRecipes();
            final int next = new Random().nextInt(NOTE_RECIPE_COUNT_IN_RESPONSE);
            Recipe recipe = recipeList.get(next);
            Note note = new Note(
                    NOTE_RECIPE_TITLE + recipe.getTitle(),
                    recipe.getIngredients() + "\n" + recipe.getWebsiteUrl(),
                    NOTE_DEFAULT_CATEGORY_ID,
                    recipe.getThumbnail()
            );
            viewModel.insertNoteInMemory(note);
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
        swipeRefreshLayout.setOnRefreshListener(this::getSomeRandomNotes);
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
                Log.i(TAG, "searchView.onQueryTextSubmit: query = " + query);
                searchSingleRandomNote(query);
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
    }

    private void getSomeRandomNotes() {
        viewModel.deleteNotesInMemory();
        for (int i = 0; i < NOTE_INIT_COUNT; ++i) {
            Log.i(TAG, "getSomeRandomNotes: fetching note " + i);
            getSingleRandomNote();
        }
        viewModel.backupNotesInMemory();
    }

    private void getSingleRandomNote() {
        int next = new Random().nextInt(NOTE_TYPE_COUNT);
        switch (next) {
            case 0:
                viewModel.getSingleJoke();
                break;
            case 1:
                viewModel.getTwoPartJoke();
                break;
            case 2:
                viewModel.getPuppyRecipesRandomly();
                break;
            default:
                Log.e(TAG, "getSingleRandomNote: unknown next id = " + next);
        }
    }

    private void searchSingleRandomNote(String query) {
        int next = new Random().nextInt(NOTE_TYPE_COUNT);
        switch (next) {
            case 0:
                viewModel.searchSingleJoke(query);
                break;
            case 1:
                viewModel.searchTwoPartJoke(query);
                break;
            default:
                Log.e(TAG, "searchSingleRandomNote: unknown next id = " + next);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
