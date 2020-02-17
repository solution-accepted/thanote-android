package edu.uci.thanote.scenes.main.fragments.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewViewHolder> {

    Note[] notes;

    HomeRecyclerViewAdapter(Note[] notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public HomeRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);

        return new HomeRecyclerViewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewViewHolder holder, int position) {
        holder.setNote(notes[position]);
    }

    @Override
    public int getItemCount() {
        return notes.length;
    }
}
