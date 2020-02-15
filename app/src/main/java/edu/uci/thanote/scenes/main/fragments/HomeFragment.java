package edu.uci.thanote.scenes.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.uci.thanote.R;
import edu.uci.thanote.scenes.note.NoteActivity;

public class HomeFragment extends Fragment {
    // TODO: - Declare private UI Components here...
    // private Button xxxButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        // TODO: - Binding Views Here...
        // xxxButton = view.findViewById(R.id.button_xxx);

        // TODO: - Remove Demo Code
        Button button = view.findViewById(R.id.button_home_detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNoteDetail();
            }
        });
    }

    private void openNoteDetail() {
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        startActivity(intent);
    }
}
