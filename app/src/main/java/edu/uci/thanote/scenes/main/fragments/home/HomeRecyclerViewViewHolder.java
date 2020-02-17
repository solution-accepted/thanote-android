package edu.uci.thanote.scenes.main.fragments.home;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;

public class HomeRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    TextView textViewTitle;
    TextView textViewDate;
    TextView textViewDescription;

    public HomeRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.text_view_note_title);
        textViewDate = itemView.findViewById(R.id.text_view_note_date);
        textViewDescription = itemView.findViewById(R.id.text_view_note_description);
    }

    public void setNote(Note note) {
        textViewDescription.setText(note.getDetail());
        textViewTitle.setText(note.getTitle());
        textViewDate.setText(note.getCreateDate().toString());
    }
}
