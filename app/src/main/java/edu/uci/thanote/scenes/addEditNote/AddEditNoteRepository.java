package edu.uci.thanote.scenes.addEditNote;

import android.app.Application;

import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.databases.note.NoteTable;

public class AddEditNoteRepository {
    private NoteTable noteTable;

    public AddEditNoteRepository(Application application) {
        noteTable = new NoteTable(application);
    }

    public void insert(Note note) {
        noteTable.insert(note);
    }

    public void update(Note note) {
        noteTable.update(note);
    }
}
