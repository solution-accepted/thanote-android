package edu.uci.thanote.scenes.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {

    NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDetail().equals(newItem.getDetail()) &&
                    oldItem.getCategoryId() == newItem.getCategoryId();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = getItem(position);
        holder.noteDate.setText(note.getCreateDate().toString());
        holder.noteTitle.setText(note.getTitle());
        holder.noteDescription.setText(note.getDetail());
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        TextView noteDate;
        TextView noteTitle;
        TextView noteDescription;

        NoteHolder(View view) {
            super(view);
            noteDate = view.findViewById(R.id.text_view_note_date);
            noteTitle = view.findViewById(R.id.text_view_note_title);
            noteDescription = view.findViewById(R.id.text_view_note_description);
        }
    }

    public Note getNote(int position) {
        return getItem(position);
    }
}
