package edu.uci.thanote.sqlite.tables;

public class CategoryTable {
    public static final String TABLE_NAME = "categories";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String[] ALL_COLUMNS = new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_CREATED_AT};
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            +");";
    public static final String INSERT_DEFAULT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + ") VALUES ('default');";
}