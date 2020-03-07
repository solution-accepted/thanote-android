package edu.uci.thanote.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.uci.thanote.sqlite.models.Category;
import edu.uci.thanote.sqlite.models.Note;
import edu.uci.thanote.sqlite.tables.CategoryTable;
import edu.uci.thanote.sqlite.tables.NoteTable;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseManager {
    private static final String[] TABLES = {CategoryTable.TABLE_NAME, NoteTable.TABLE_NAME};
    private static final String[] CREATE_ALL_TABLE_QUERIES = {CategoryTable.CREATE_TABLE, CategoryTable.INSERT_DEFAULT, NoteTable.CREATE_TABLE};

    private SQLiteDatabaseHelper databaseHelper;

    public SQLiteDatabaseManager(Context context) {
        this.databaseHelper = new SQLiteDatabaseHelper(context, TABLES, CREATE_ALL_TABLE_QUERIES);
    }

    // region public Category Table methods

    public int insertCategory(Category category) { // return new id or -1 if failed
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String name = replaceSpecialCharacter(category.getName());
        ContentValues categoryContentValues = makeCategoryContentValues(name);

        int id = (int) db.insert(CategoryTable.TABLE_NAME, null, categoryContentValues);
        db.close();

        return id;
    }

    public void updateCategory(Category category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int id = category.getId();
        String name = replaceSpecialCharacter(category.getName());
        ContentValues categoryContentValues = makeCategoryContentValues(name);

        String whereClause = " " + CategoryTable.COLUMN_ID + " = ?";
        db.update(CategoryTable.TABLE_NAME, categoryContentValues, whereClause, new String[] {String.valueOf(id)});
        db.close();
    }

    public void deleteCategory(Category category) {
        int id = category.getId();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String whereClause = " " + CategoryTable.COLUMN_ID + " = ?";
        db.delete(CategoryTable.TABLE_NAME, whereClause, new String[] {String.valueOf(id)});
        db.close();
    }

    public List<Category> getCategories() {
        // query: SELECT * FROM categories;
        return readCategoryData(null, null, null);
    }

    public List<Category> getCategories(String keyword) {
        // query: SELECT * FROM categories WHERE name LIKE '%keyword%'
        String selection = CategoryTable.COLUMN_NAME + " LIKE ?";
        String filter = "%"+ replaceSpecialCharacter(keyword) + "%";
        String[] selectionArgs = new String[] {filter};
        return readCategoryData(selection, selectionArgs, null);
    }

    public Category getCategory(int id) {
        // query: SELECT * FROM categories WHERE _id = id
        String selection = CategoryTable.COLUMN_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        List<Category> items = readCategoryData(selection, selectionArgs, null);
        return items.size() > 0 ? items.get(0) : null;
    }

    // endregion

    // region public Note Table methods

    public int insertNote(Note note) { // return new id or -1 if failed
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String title = replaceSpecialCharacter(note.getTitle());
        String detail = replaceSpecialCharacter(note.getDetail());
        String imageUrl = replaceSpecialCharacter(note.getImageUrl());
        int categoryId = note.getCategoryId();
        ContentValues noteContentValues = makeNoteContentValues(title, detail, categoryId, imageUrl);

        int id = (int) db.insert(NoteTable.TABLE_NAME, null, noteContentValues);
        db.close();

        return id;
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int id = note.getId();
        String title = replaceSpecialCharacter(note.getTitle());
        String detail = replaceSpecialCharacter(note.getDetail());
        String imageUrl = replaceSpecialCharacter(note.getImageUrl());
        int categoryId = note.getCategoryId();
        ContentValues noteContentValues = makeNoteContentValues(title, detail, categoryId, imageUrl);

        String whereClause = " " + NoteTable.COLUMN_ID + " = ?";
        db.update(NoteTable.TABLE_NAME, noteContentValues, whereClause, new String[] {String.valueOf(id)});
        db.close();
    }

    public void deleteNote(Note note) {
        int id = note.getId();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String whereClause = " " + NoteTable.COLUMN_ID + " = ?";
        db.delete(NoteTable.TABLE_NAME, whereClause, new String[] {String.valueOf(id)});
        db.close();
    }

    public List<Note> getNotes() {
        // query: SELECT * FROM notes;
        return readNoteData(null, null, null);
    }

    public List<Note> getNotesByTitle(String keyword) {
        // query: SELECT * FROM notes WHERE title LIKE '%keyword%'
        String selection = NoteTable.COLUMN_TITLE + " LIKE ?";
        String filter = "%"+ replaceSpecialCharacter(keyword) + "%";
        String[] selectionArgs = new String[] {filter};
        return readNoteData(selection, selectionArgs, null);
    }

    public List<Note> getNotesByDetail(String keyword) {
        // query: SELECT * FROM notes WHERE detail LIKE '%keyword%'
        String selection = NoteTable.COLUMN_DETAIL + " LIKE ?";
        String filter = "%"+ replaceSpecialCharacter(keyword) + "%";
        String[] selectionArgs = new String[] {filter};
        return readNoteData(selection, selectionArgs, null);
    }

    public Note getNote(int id) {
        // query: SELECT * FROM notes WHERE _id = id
        String selection = NoteTable.COLUMN_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        List<Note> items = readNoteData(selection, selectionArgs, null);
        return items.size() > 0 ? items.get(0) : null;
    }

    // endregion

    // region private methods

    private String replaceSpecialCharacter(String s) {
        // replace ' to \\'
        return s.replaceAll("'","\\'");
    }

    private ContentValues makeCategoryContentValues(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CategoryTable.COLUMN_NAME, name);
        return contentValues;
    }

    private ContentValues makeNoteContentValues(String title, String detail, int categoryId, String imageUrl) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteTable.COLUMN_TITLE, title);
        contentValues.put(NoteTable.COLUMN_DETAIL, detail);
        contentValues.put(NoteTable.COLUMN_CATEGORY_ID, categoryId);
        contentValues.put(NoteTable.COLUMN_IMAGE_URL, imageUrl);
        return contentValues;
    }

    private List<Category> readCategoryData(String selection, String[] selectionArgs, String orderby) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(CategoryTable.TABLE_NAME, CategoryTable.ALL_COLUMNS, selection, selectionArgs, null, null, orderby);

        List<Category> categories = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(CategoryTable.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(CategoryTable.COLUMN_NAME));
            Category category = new Category(name);
            category.setId(id);
            categories.add(category);
        }

        cursor.close();
        db.close();

        return categories;
    }

    private List<Note> readNoteData(String selection, String[] selectionArgs, String orderby) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NoteTable.TABLE_NAME, CategoryTable.ALL_COLUMNS, selection, selectionArgs, null, null, orderby);

        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(NoteTable.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(NoteTable.COLUMN_TITLE));
            String detail = cursor.getString(cursor.getColumnIndex(NoteTable.COLUMN_DETAIL));
            int categoryId = cursor.getInt(cursor.getColumnIndex(NoteTable.COLUMN_CATEGORY_ID));
            String imageUrl = cursor.getString(cursor.getColumnIndex(NoteTable.COLUMN_IMAGE_URL));
            Note note = new Note(title, detail, categoryId, imageUrl);
            note.setId(id);
            notes.add(note);
        }

        cursor.close();
        db.close();

        return notes;
    }

    // endregion
}
