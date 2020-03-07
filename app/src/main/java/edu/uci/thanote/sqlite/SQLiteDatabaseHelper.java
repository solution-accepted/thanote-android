package edu.uci.thanote.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    // Database Information
    private static final String DATABASE_NAME = "thanote_sql_database.db";
    private static final int DATABASE_VERSION = 1;

    private String[] tables;
    private String[] onCreateQueries;

    public SQLiteDatabaseHelper(@Nullable Context context, String[] tables, String[] onCreateQueries) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.tables = tables;
        this.onCreateQueries = onCreateQueries;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String query: onCreateQueries) {
            db.execSQL(query);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // simply drop old tables and create new tables
        for (String table: tables) {
            db.execSQL("DROP TABLE IF EXISTS "+ table);
        }

        onCreate(db);
    }
}
