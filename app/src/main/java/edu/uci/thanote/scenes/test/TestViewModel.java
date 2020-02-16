package edu.uci.thanote.scenes.test;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.note.Note;

import java.util.List;
import java.util.Random;

public class TestViewModel extends AndroidViewModel {
    private final TestRepository repository;
    private LiveData<List<Category>> categories;
    private LiveData<List<Note>> notes;

    public TestViewModel(@NonNull Application application) {
        super(application);
        repository = new TestRepository(application);
        categories = repository.getCategories();
        notes = repository.getNotes();
    }

    // region Public APIs

    public void insertCategory(Category category) {
        if (!isValidCategory(category)) {
            listener.didVerifyError("some insert error occur...");
            return;
        }
        repository.insertCategory(category);
    }

    public void updateCategory(Category category) {
        if (!isValidCategory(category)) {
            listener.didVerifyError("some update error occur...");
            return;
        }
        repository.updateCategory(category);
    }

    public void deleteCategory(Category category) {
        if (!isValidCategory(category)) {
            listener.didVerifyError("some delete error occur...");
            return;
        }
        repository.deleteCategory(category);
    }

    public void deleteAllCategories() {
        repository.deleteAllCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void insertNote(Note note) {
        if (!isValidNote(note)) {
            listener.didVerifyError("some insert error occur...");
            return;
        }
        repository.insertNote(note);
    }

    public void updateNote(Note note) {
        if (!isValidNote(note)) {
            listener.didVerifyError("some update error occur...");
            return;
        }
        repository.updateNote(note);
    }

    public void deleteNote(Note note) {
        if (!isValidNote(note)) {
            listener.didVerifyError("some delete error occur...");
            return;
        }
        repository.deleteNote(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    // endregion

    // TODO: - Must define listener to notify view change state or messages
    // region ViewModel Listener

    private TestViewModelListener listener;

    public interface TestViewModelListener {
        void didVerifyError(String message);
    }

    public void setListener(TestViewModelListener listener) {
        this.listener = listener;
    }

    // endregion

    // TODO: - Must implement some verify method before processing data to DB
    private boolean isValidCategory(Category category) {
        return true;
    }
    private boolean isValidNote(Note note) {
        return true;
    }

    // TODO: - Only for test, will delete later...
    // region Test

    public static final int CATEGORY_TABLE = 0;
    public static final int NOTE_TABLE = 1;
    private final int testTable = NOTE_TABLE;

    public void testInsert() {
        if (testTable == CATEGORY_TABLE) {
            Category category = new Category("Test " + (int) (Math.random() * 100));
            insertCategory(category);
        } else {
            int number = (int) (Math.random() * 100);
            String imageUrl = (number % 2 == 0) ? "https://www.google.com/some_image.png" : "";
            String title = "title" + number;
            String detail = "detail" + number;

            // category id = 1: default
            Note note = new Note(title, detail, 1, imageUrl);
            insertNote(note);
        }
    }

    public void testUpdate() {
        if (testTable == CATEGORY_TABLE) {
            Category category = new Category("Nice");
            category.setId(2);
            updateCategory(category);
        } else {
            int number = (int) (Math.random() * 100);
            String imageUrl = (number % 2 == 0) ? "https://www.google.com/some_image.png" : "";
            String title = "title" + number;
            String detail = "detail" + number;

            // category id = 1: default
            Note note = new Note(title, detail, 1, imageUrl);
            note.setId(1);
            updateNote(note);
        }
    }

    public void testDelete() {
        if (testTable == CATEGORY_TABLE) {
            Category category = new Category("Nice");
            category.setId(2);
            deleteCategory(category);
        } else {

        }
    }

    public void testDeleteAll() {
        if (testTable == CATEGORY_TABLE) {
            deleteAllCategories();
        } else {
            deleteAllNotes();
        }
    }

    // endregion
}
