package edu.uci.thanote.scenes.main.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.scenes.test.TestActivity;

public class HomeFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        searchView = view.findViewById(R.id.search_view_home);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());

//        Also try:
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(false);

        final Note[] NOTES = {
                new Note("note1", "note1", 1, ""),
                new Note("note2", "note2", 2, ""),
                new Note("note3", "note3", 3, ""),
        };
        recyclerView = view.findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new HomeRecyclerViewAdapter(NOTES));
    }

    private void openNoteDetail() {
//        Intent intent = new Intent(getActivity(), NoteActivity.class);
        Intent intent = new Intent(getActivity(), TestActivity.class);
        startActivity(intent);
    }
}
