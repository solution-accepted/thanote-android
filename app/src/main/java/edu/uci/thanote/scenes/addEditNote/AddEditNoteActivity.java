package edu.uci.thanote.scenes.addEditNote;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.scenes.test.BaseActivity;

public class AddEditNoteActivity extends BaseActivity {
    public static final String EXTRA_NOTE =
            "edu.uci.thanote.EXTRA_NOTE";
    public static final String CATEGORY_ID =
            "edu.uci.thanote.CATEGORY_ID";

    private final String HEADER_EDIT_NOTE = "Edit Note";
    private final String HEADER_ADD_NOTE = "Add Note";
    private final String EMPTY_WARNING = "Please insert note title and note detail";


    private EditText editTextNoteTitle;
    private EditText editTextNoteDetail;
    private int categoryId = -1;
    private AddEditNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);
        setupViews();
        setupViewModel();
    }

    public void setupViews() {
        editTextNoteTitle = findViewById(R.id.edit_text_new_note_title);
        editTextNoteDetail = findViewById(R.id.edit_text_new_note_detail);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE)) {
            setTitle(HEADER_EDIT_NOTE);
            Note note = (Note) intent.getSerializableExtra(EXTRA_NOTE);

            editTextNoteTitle.setText(note.getTitle());
            editTextNoteDetail.setText(note.getDetail());
        } else {
            setTitle(HEADER_ADD_NOTE);
            categoryId = intent.getIntExtra(CATEGORY_ID, -1);
        }

    }

    public void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AddEditNoteViewModel.class);
    }

    private void saveNote() {
        String newNoteTitle = editTextNoteTitle.getText().toString();
        String newNoteDetail = editTextNoteDetail.getText().toString();

        if (newNoteTitle.trim().isEmpty() || newNoteDetail.trim().isEmpty()) {
            showShortToast(EMPTY_WARNING);
            return;
        }

        Intent intent = getIntent();

        if (intent.hasExtra(AddEditNoteActivity.EXTRA_NOTE)) {
            Note note = (Note) intent.getSerializableExtra(AddEditNoteActivity.EXTRA_NOTE);
            note.setTitle(newNoteTitle);
            note.setDetail(newNoteDetail);
            viewModel.update(note);
        } else if (categoryId != -1) {
            Note note = new Note(newNoteTitle, newNoteDetail, categoryId, "");
            viewModel.insert(note);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveNote();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
