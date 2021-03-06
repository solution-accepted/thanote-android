package edu.uci.thanote.scenes.noteList;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.uci.thanote.R;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.scenes.addCollection.AddCollectionActivity;
import edu.uci.thanote.scenes.addEditNote.AddEditNoteActivity;
import edu.uci.thanote.scenes.general.BaseActivity;

public class NoteListActivity extends BaseActivity {
    public static final String EXTRA_CATEGORY =
            "edu.uci.thanote.EXTRA_CATEGORY";
    public static final int CATEGORY_ID_DEFAULT = 0;
    public final String SHARE_INTENT_TITLE = "Share Note Via:";

    private Category category;

    private RecyclerView recyclerView;
    private SearchView searchView;
    private TextView textView;

    private FloatingActionButton buttonAddNote;

    private NoteListViewModel viewModel;
    private final NoteListAdapter adapter = new NoteListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);

        setupViewModel();
        setupViews();

    }

    public void setAddNoteListener() {
        buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.CATEGORY_ID, category.getId());
                startActivity(intent);
            }
        });
    }

    public void setEditNoteListener() {
        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {

                Intent intent = new Intent(NoteListActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_NOTE, note);
                startActivity(intent);
            }

            @Override
            public void onButtonShareClick(Note note) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, note.getTitle() + "\n" + note.getDetail());
                intent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(intent, SHARE_INTENT_TITLE);
                startActivity(shareIntent);
            }

        });
    }

    public void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(NoteListViewModel.class);
        viewModel.setCategoryId(category.getId());
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
        getSupportActionBar().hide();

        textView = findViewById(R.id.text_view_note_list_header);
        textView.setText(category.getName());

        recyclerView = findViewById(R.id.recycler_view_note_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setAddNoteListener();
        setEditNoteListener();
        createDeleteView();

        searchView = findViewById(R.id.search_view_note_list);
        searchView.setBackgroundResource(R.color.super_light_gray);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.clearFocus();

        searchView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                buttonAddNote.hide();
            }
        });

        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.searchView.findViewById(searchCloseButtonId);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(true);
                buttonAddNote.show();
            }
        });

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
        openPage(AddCollectionActivity.class);
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
            }
        }).attachToRecyclerView(recyclerView);
    }
}