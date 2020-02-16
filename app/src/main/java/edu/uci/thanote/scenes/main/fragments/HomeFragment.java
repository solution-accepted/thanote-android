package edu.uci.thanote.scenes.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.uci.thanote.R;
import edu.uci.thanote.scenes.note.NoteActivity;
import edu.uci.thanote.scenes.test.TestActivity;

public class HomeFragment extends Fragment {
    // TODO: - Declare private UI Components here...
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        // TODO: - Binding Views Here...
        searchView = view.findViewById(R.id.search_view_home);
        searchView.setSubmitButtonEnabled(true);
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(false);
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());
    }

    private void openNoteDetail() {
//        Intent intent = new Intent(getActivity(), NoteActivity.class);
        Intent intent = new Intent(getActivity(), TestActivity.class);
        startActivity(intent);
    }
}
