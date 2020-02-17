package edu.uci.thanote.scenes.main.fragments.home;

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
import edu.uci.thanote.R;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.databases.note.Note;

import java.util.Random;

public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";

    private final int NOTE_INIT_COUNT = 1;
    private final int NOTE_TYPE_COUNT = 2;

    private HomeViewModel viewModel;

    private SearchView searchView;
    private RecyclerView recyclerView;
    private HomeRecyclerViewAdapter recyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViewModel();
        setupViews(view);
        getSomeNotes();
        return view;
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.setListener(new HomeViewModel.Listener() {
            @Override
            public void didFetchSingleJoke(SingleJoke joke) {
                // TODO show notes without inserting
                viewModel.insertNote(new Note("Joke", joke.getJoke(), 0, null));
            }

            @Override
            public void didFetchTwoPartJoke(TwoPartJoke joke) {
                viewModel.insertNote(new Note("Joke", joke.getSetup() + " " + joke.getDelivery(), 0, null));
            }

            @Override
            public void didFetchSingleJokeByKey(SingleJoke joke) {
                didFetchSingleJoke(joke);
            }

            @Override
            public void didFetchTwoPartJokeByKey(TwoPartJoke joke) {
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
        viewModel.getNotes().observe(getViewLifecycleOwner(),
                notes -> recyclerViewAdapter.setNotes(notes));
    }

    private void setupViews(View view) {
        searchView = view.findViewById(R.id.search_view_home);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());
        searchView.setOnSearchClickListener(v -> {
            final String query = searchView.getQuery().toString();
            showToast("searchView.OnSearchClickListener: query = " + query);
            Log.i(TAG, "searchView.OnSearchClickListener: query = " + query);
            viewModel.searchSingleJoke(query);
        });

//        Also try:
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(false);

        recyclerViewAdapter = new HomeRecyclerViewAdapter(this);
        recyclerView = view.findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void getSomeNotes() {
        Random random = new Random();
        for (int i = 0; i < NOTE_INIT_COUNT; ++i) {
            int next = random.nextInt(NOTE_TYPE_COUNT);
            Log.i(TAG, "getSomeNotes: fetching note " + i + " next = " + next);
            switch (next) {
                case 0:
                    viewModel.getSingleJoke();
                    break;
                case 1:
                    viewModel.getTwoPartJoke();
                    break;
                default:
                    Log.e(TAG, "getSomeNotes: unknown next id = " + next);
                    return;
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public HomeViewModel getViewModel() {
        return viewModel;
    }
}
