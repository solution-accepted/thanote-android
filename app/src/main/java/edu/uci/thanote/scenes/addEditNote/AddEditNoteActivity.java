package edu.uci.thanote.scenes.addEditNote;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.uci.thanote.R;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.myapplication.EXTRA_ID";
    public static final String EXTRA_NOTE_TITLE =
            "com.example.myapplication.EXTRA_NOTE_TITLE";
    public static final String EXTRA_NOTE_DETAIL =
            "com.example.myapplication.EXTRA_NOTE_DETAIL";

    private EditText editTextNoteTitle;
    private EditText editTextNoteDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);
        setupViews();
    }

    public void setupViews() {
        editTextNoteTitle = findViewById(R.id.edit_text_new_note_title);
        editTextNoteDetail = findViewById(R.id.edit_text_new_note_detail);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextNoteTitle.setText(intent.getStringExtra(EXTRA_NOTE_TITLE));
            editTextNoteDetail.setText(intent.getStringExtra(EXTRA_NOTE_DETAIL));
        } else {
            setTitle("Add Note");
        }
        
    }

    private void saveNote() {
        String newNoteTitle = editTextNoteTitle.getText().toString();
        String newNoteDetail = editTextNoteDetail.getText().toString();

        if (newNoteTitle.trim().isEmpty() || newNoteDetail.trim().isEmpty()) {
            Toast.makeText(this, "Please insert note title and note detail", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NOTE_TITLE, newNoteTitle);
        data.putExtra(EXTRA_NOTE_DETAIL, newNoteDetail);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }
}
