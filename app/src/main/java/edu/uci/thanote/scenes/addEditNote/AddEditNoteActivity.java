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
            "com.example.myapplication.EXTRA_NOTE";
    public static final String CATEGORY_ID =
            "com.example.myapplication.CATEGORY_ID";


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
            setTitle("Edit Note");
            Note note = (Note) intent.getSerializableExtra("EXTRA_NOTE");

            editTextNoteTitle.setText(note.getTitle());
            editTextNoteDetail.setText(note.getDetail());
        } else {
            setTitle("Add Note");
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
            showShortToast("Please insert note title and note detail");
            return;
        }

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_NOTE)) {
            Note note = (Note) getIntent().getSerializableExtra("EXTRA_NOTE");
            note.setTitle(newNoteTitle);
            note.setDetail(newNoteDetail);
            viewModel.update(note);
            showShortToast("Note updated");
        } else if (categoryId != -1) {
            Note note = new Note(newNoteTitle, newNoteDetail, categoryId, "");
            viewModel.insert(note);
            showShortToast("Note saved");
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
                showShortToast("No note saved");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        showShortToast("No note saved");
        finish();
        return false;
    }
}
