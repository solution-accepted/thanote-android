package edu.uci.thanote.scenes.main.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import edu.uci.thanote.databases.note.Note;

import java.util.Random;

public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";

    private final int NOTE_INIT_COUNT = 10;
    private final int NOTE_TYPE_COUNT = 2;
    private final int NOTE_DEFAULT_CATEGORY_ID = 1;

    private HomeViewModel viewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
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

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.setListener(new HomeViewModel.Listener() {
            @Override
            public void didFetchSingleJoke(SingleJoke joke) {
                swipeRefreshLayout.setRefreshing(false);
                Note newNote = new Note("Joke", joke.getJoke(), NOTE_DEFAULT_CATEGORY_ID, "");
                viewModel.insertNoteInMemory(newNote);
            }

            @Override
            public void didFetchTwoPartJoke(TwoPartJoke joke) {
                swipeRefreshLayout.setRefreshing(false);
                Note newNote = new Note("Joke", joke.getSetup() + "\n" + joke.getDelivery(), NOTE_DEFAULT_CATEGORY_ID, "");
                viewModel.insertNoteInMemory(newNote);
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
            public void didFetchError(String message) {
                showToast(message);
            }

            @Override
            public void didVerifyError(String message) {
                showToast(message);
            }
        });
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
                    getSomeRandomNotes();
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
