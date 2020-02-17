package edu.uci.thanote.scenes.main.fragments.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;

public class HomeRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    private HomeFragment homeFragment;
    private HomeViewModel homeViewModel;

    TextView textViewTitle;
    TextView textViewDate;
    TextView textViewDescription;

    ImageButton buttonShare;
    ImageButton buttonFavorite;

    boolean isFavorite = false;

    public HomeRecyclerViewViewHolder(@NonNull View itemView, HomeFragment homeFragment) {
        super(itemView);

        textViewTitle = itemView.findViewById(R.id.text_view_note_title);
        textViewDate = itemView.findViewById(R.id.text_view_note_date);
        textViewDescription = itemView.findViewById(R.id.text_view_note_description);

        buttonShare = itemView.findViewById(R.id.button_note_share);
        buttonFavorite = itemView.findViewById(R.id.button_note_favorite);

        this.homeFragment = homeFragment;
        this.homeViewModel = homeFragment.getViewModel();
    }

    public void setNote(Note note) {
        textViewDescription.setText(note.getDetail());
        textViewTitle.setText(note.getTitle());
        textViewDate.setText("");

        buttonShare.setOnClickListener(v -> {
            Intent intent = new Intent("ACTION_SHARE");
            homeFragment.startActivity(intent);
        });

        buttonFavorite.setOnClickListener(v -> {
            if (isFavorite) {
                homeViewModel.deleteNote(note);
            } else {
                homeViewModel.insertNote(note);
            }
        });
    }
}
