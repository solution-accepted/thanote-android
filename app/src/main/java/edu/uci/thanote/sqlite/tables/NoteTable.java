package edu.uci.thanote.sqlite.tables;

public class NoteTable {
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DETAIL = "detail";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_IMAGE_URL = "imageUrl";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String[] ALL_COLUMNS = new String[] {COLUMN_ID, COLUMN_TITLE, COLUMN_DETAIL, COLUMN_CATEGORY_ID, COLUMN_IMAGE_URL, COLUMN_CREATED_AT};
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT NOT NULL, "
            + COLUMN_DETAIL + " TEXT NOT NULL, "
            + COLUMN_CATEGORY_ID + " INTEGER, "
            + COLUMN_IMAGE_URL + " TEXT NOT NULL, "
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            +");";
}
