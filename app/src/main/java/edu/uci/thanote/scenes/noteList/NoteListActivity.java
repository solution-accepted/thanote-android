package edu.uci.thanote.scenes.noteList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.scenes.addnote.AddNoteActivity;

public class NoteListActivity extends AppCompatActivity {
    private final int ADD_NOTE_REQUEST = 1;

    private RecyclerView recyclerView;
    private NoteListViewModel viewModel;
    private final NoteListAdapter adapter = new NoteListAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setupViewModel();
        setupViews();
    }

    public void addNote() {
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
    }

    public void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(NoteListViewModel.class);
        viewModel.setListener(new NoteListViewModel.NoteViewModelListener() {
            @Override
            public void onNoteClick(Note note) {
                openNote();
            }
        });

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.note_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        addNote();
        createDeleteView();
    }

    private void openNote() {
        Intent intent = new Intent(this, AddNoteActivity.class); //todo
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteActivity.EXTRA_NOTE_TITLE);
            String detail = data.getStringExtra(AddNoteActivity.EXTRA_NOTE_DETAIL);

            Note note = new Note(title, detail, 1, ""); // todo
            viewModel.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }

    }

    public void createDeleteView() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
//                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}