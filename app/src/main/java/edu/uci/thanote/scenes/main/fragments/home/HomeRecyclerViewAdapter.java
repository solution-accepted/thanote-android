package edu.uci.thanote.scenes.main.fragments.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;

import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewViewHolder> {

    HomeFragment homeFragment;
    List<Note> notes;

    HomeRecyclerViewAdapter() {
        this.notes = new ArrayList<>();
    }

    HomeRecyclerViewAdapter(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
        this.notes = new ArrayList<>();
    }

    HomeRecyclerViewAdapter(List<Note> notes) {
        this.notes = notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);

        return new HomeRecyclerViewViewHolder(v, homeFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewViewHolder holder, int position) {
        holder.setNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
