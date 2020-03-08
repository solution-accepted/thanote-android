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
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.scenes.addEditNote.AddEditNoteActivity;

public class NoteListActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    public static final String CATEGORY_ID = "Category_id";
    public static final int CATEGORY_ID_DEFAULT = 0;

    private int categoryId;
    private RecyclerView recyclerView;
    private NoteListViewModel viewModel;
    private final NoteListAdapter adapter = new NoteListAdapter();

    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        Intent intent = getIntent();
        categoryId = intent.getIntExtra(CATEGORY_ID, CATEGORY_ID_DEFAULT);

        setupViewModel();
        setupViews();

    }

    public void setAddNoteListener() {
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
    }

    public void setEditNoteListener() {
        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(NoteListActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_DETAIL, note.getDetail());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    public void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(NoteListViewModel.class);
        viewModel.setCategoryId(categoryId);
        viewModel.setListener(new NoteListViewModel.NoteViewModelListener() {
            @Override
            public void onNoteClick(Note note) {
                openNote();
            }
        });

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
                adapter.submitList(notes);
            }
        });
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.recycler_view_note_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setAddNoteListener();
        setEditNoteListener();
        createDeleteView();

        searchView = findViewById(R.id.search_view_note_list);
//        searchView.setSubmitButtonEnabled(true);
//        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());
        searchView.setBackgroundResource(R.color.super_light_gray);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    private void openNote() {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_NOTE_TITLE);
            String detail = data.getStringExtra(AddEditNoteActivity.EXTRA_NOTE_DETAIL);

            Note note = new Note(title, detail, categoryId, "");
            viewModel.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_NOTE_TITLE);
            String detail = data.getStringExtra(AddEditNoteActivity.EXTRA_NOTE_DETAIL);

            Note note = new Note(title, detail, categoryId, "");
            note.setId(id);
            viewModel.update(note);

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No note saved", Toast.LENGTH_SHORT).show();
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