package edu.uci.thanote.scenes.test;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.category.CategoryTable;
import edu.uci.thanote.databases.note.Note;
import edu.uci.thanote.databases.note.NoteTable;

import java.util.List;

public class TestRepository {
    // database tables
    private CategoryTable categoryTable;
    private NoteTable noteTable;

    // data fields
    private LiveData<List<Category>> categories;
    private LiveData<List<Note>> notes;

    public TestRepository(Application application) {
        categoryTable = new CategoryTable(application);
        categories = categoryTable.getCategories();

        noteTable = new NoteTable(application);
        notes = noteTable.getNotes();
    }

    // region Public APIs

    public void insertCategory(Category category) {
        categoryTable.insert(category);
    }

    public void updateCategory(Category category) {
        categoryTable.update(category);
    }

    public void deleteCategory(Category category) {
        categoryTable.delete(category);
    }

    public void deleteAllCategories() {
        categoryTable.deleteAllCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void insertNote(Note note) {
        noteTable.insert(note);
    }

    public void updateNote(Note note) {
        noteTable.update(note);
    }

    public void deleteNote(Note note) {
        noteTable.delete(note);
    }

    public void deleteAllNotes() {
        noteTable.deleteAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    // endregion
}
