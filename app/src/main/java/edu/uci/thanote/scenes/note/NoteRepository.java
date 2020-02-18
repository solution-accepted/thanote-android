package edu.uci.thanote.scenes.note;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.databases.note.NoteTable;


public class NoteRepository {
    private NoteRepository.NoteRepositoryListener listener;
    // database tables
    private NoteTable noteTable;
    // data fields
    private LiveData<List<Note>> notes;

    public NoteRepository(Application application) {
        noteTable = new NoteTable(application);
        notes = noteTable.getNotes();
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
        return notes;
    }

    public interface NoteRepositoryListener {
        void onNoteClick(Note note);
    }
    public void setListener(NoteRepository.NoteRepositoryListener listener) {
        this.listener = listener;
    }


}
