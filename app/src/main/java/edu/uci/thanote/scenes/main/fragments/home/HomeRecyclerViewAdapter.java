package edu.uci.thanote.scenes.main.fragments.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;

import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewViewHolder> {

    private List<Note> notes;

    HomeRecyclerViewAdapter() {
        this.notes = new ArrayList<>();
    }

    HomeRecyclerViewAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public HomeRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);

        HomeRecyclerViewViewHolder holder =
                new HomeRecyclerViewViewHolder(v);

        holder.setListener(new HomeRecyclerViewViewHolder.Listener() {
            @Override
            public void onButtonShareClicked(Note note) {
                listener.onButtonShareClicked(note);
            }

            @Override
            public void onButtonFavoriteClicked(Note note, ImageButton buttonFavorite) {
                listener.onButtonFavoriteClicked(note, buttonFavorite);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewViewHolder holder, int position) {
        holder.setNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public interface Listener {
        void onButtonShareClicked(Note note);

        void onButtonFavoriteClicked(Note note, ImageButton buttonFavorite);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
