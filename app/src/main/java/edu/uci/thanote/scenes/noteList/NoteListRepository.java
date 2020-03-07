package edu.uci.thanote.scenes.noteList;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.databases.note.NoteTable;

public class NoteListRepository {
    // database tables
    private NoteTable noteTable;
    // data fields
    private LiveData<List<Note>> notes;

    public NoteListRepository(Application application) {
        noteTable = new NoteTable(application);
    }

    public void insert(Note note) {
        noteTable.insert(note);
    }

    public void update(Note note) {
        noteTable.update(note);
    }

    public void delete(Note note) {
        noteTable.delete(note);
    }

    public void deleteAllNotes() {
        noteTable.deleteAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        notes = noteTable.getNotes();
        return notes;
    }

    public void setCategoryName(String categoryName) {
        noteTable.setCategoryName(categoryName);
    }


}
