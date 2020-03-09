package edu.uci.thanote.scenes.addEditNote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import edu.uci.thanote.databases.note.Note;

public class AddEditNoteViewModel extends AndroidViewModel {
    private final AddEditNoteRepository repository;

    public AddEditNoteViewModel(@NonNull Application application) {
        super(application);
        repository = new AddEditNoteRepository(application);
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }
}
