package edu.uci.thanote.scenes.note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.uci.thanote.databases.note.Note;

public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository repository;
    private LiveData<List<Note>> notes;
    private NoteViewModel.NoteViewModelListener listener;

    public interface NoteViewModelListener {
        void onNoteClick(Note note);
    }

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        repository.setListener(noteRepositoryListener);
        notes = repository.getNotes();
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
        return notes;
    }

    public void setListener(NoteViewModel.NoteViewModelListener listener) {
        this.listener = listener;
    }

    private NoteRepository.NoteRepositoryListener noteRepositoryListener = new NoteRepository.NoteRepositoryListener() {
        @Override
        public void onNoteClick(Note note) {
            // todo

        }
    };

}
