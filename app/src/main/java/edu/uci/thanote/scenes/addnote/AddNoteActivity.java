package edu.uci.thanote.scenes.addnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.uci.thanote.R;

public class AddNoteActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE_TITLE =
            "com.example.myapplication.EXTRA_NOTE_TITLE";
    public static final String EXTRA_NOTE_DETAIL =
            "com.example.myapplication.EXTRA_NOTE_DETAIL";

    private EditText noteTitle;
    private EditText noteDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setupViews();
    }

    public void setupViews() {
        noteTitle = findViewById(R.id.new_note_title);
        noteDetail = findViewById(R.id.new_note_detail);

    }

    private void saveNote() {
        String newNoteTitle = noteTitle.getText().toString();
        String newNoteDetail = noteDetail.getText().toString();

        if (newNoteTitle.trim().isEmpty() || newNoteDetail.trim().isEmpty()) {
            Toast.makeText(this, "Please insert note title and note detail", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NOTE_TITLE, newNoteTitle);
        data.putExtra(EXTRA_NOTE_DETAIL, newNoteDetail);

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
