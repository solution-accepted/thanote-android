package edu.uci.thanote.scenes.test.sqlite.category;

import android.app.Application;
import edu.uci.thanote.sqlite.models.Category;
import edu.uci.thanote.sqlite.models.Note;
import edu.uci.thanote.sqlite.SQLiteDatabaseManager;

import java.util.List;

// This repository only demo the case with sql database
// so we don't need any listener here
// if you want to add retrofit support, please see TestRepository
public class CategoryTableTestRepository {
    private SQLiteDatabaseManager databaseManager;

    public CategoryTableTestRepository(Application application) {
        // initial database manager
        databaseManager = new SQLiteDatabaseManager(application);
    }

    // region Public APIs (Database)

    public int insertCategory(Category category) {
        return databaseManager.insertCategory(category);
    }

    public void updateCategory(Category category) {
        databaseManager.updateCategory(category);
    }

    public void deleteCategory(Category category) {
        databaseManager.deleteCategory(category);
    }

    public List<Category> getCategories() {
        return databaseManager.getCategories();
    }

    public List<Category> getCategories(String keyword) {
        return databaseManager.getCategories(keyword);
    }

    public Category getCategory(int id) {
        return databaseManager.getCategory(id);
    }

//    public int insertNote(Note note) {
//        return databaseManager.insertNote(note);
//    }
//
//    public void updateNote(Note note) {
//        databaseManager.updateNote(note);
//    }
//
//    public void deleteNote(Note note) {
//        databaseManager.deleteNote(note);
//    }
//
//    public List<Note> getNotes() {
//        return databaseManager.getNotes();
//    }
//
//    public List<Note> getNotesByTitle(String title) {
//        return databaseManager.getNotesByTitle(title);
//    }
//
//    public List<Note> getNotesByDetail(String detail) {
//        return databaseManager.getNotesByTitle(detail);
//    }
//
//    public Note getNote(int id) {
//        return databaseManager.getNote(id);
//    }

    // endregion
}
