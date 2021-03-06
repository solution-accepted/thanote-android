package edu.uci.thanote.scenes.noteList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.uci.thanote.databases.note.Note;

public class NoteListViewModel extends AndroidViewModel {
    private final NoteListRepository repository;
    private LiveData<List<Note>> notes;
    private NoteViewModelListener listener;

    public NoteListViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteListRepository(application);
    }

    public void setCategoryId(int categoryId) {
        repository.setCategoryId(categoryId);
    }
    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        notes = repository.getNotes();
        return notes;
    }

    public interface NoteViewModelListener {
        void onNoteClick(Note note);
    }

    public void setListener(NoteViewModelListener listener) {
        this.listener = listener;
    }
}
