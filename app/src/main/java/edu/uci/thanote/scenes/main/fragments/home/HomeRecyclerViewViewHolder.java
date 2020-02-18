package edu.uci.thanote.scenes.main.fragments.home;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;

public class HomeRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    private TextView textViewTitle;
    private TextView textViewDate;
    private TextView textViewDescription;

    private ImageButton buttonShare;
    private ImageButton buttonFavorite;

    public HomeRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewTitle = itemView.findViewById(R.id.text_view_note_title);
        textViewDate = itemView.findViewById(R.id.text_view_note_date);
        textViewDescription = itemView.findViewById(R.id.text_view_note_description);

        buttonShare = itemView.findViewById(R.id.button_note_share);
        buttonFavorite = itemView.findViewById(R.id.button_note_favorite);
    }

    public void setNote(Note note) {
        textViewDescription.setText(note.getDetail());
        textViewTitle.setText(note.getTitle());
        textViewDate.setText("");

        buttonShare.setOnClickListener(v -> listener.onButtonShareClicked(note));
        buttonFavorite.setOnClickListener(v -> listener.onButtonFavoriteClicked(note));
    }

    public interface Listener {
        void onButtonShareClicked(Note note);

        void onButtonFavoriteClicked(Note note);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
