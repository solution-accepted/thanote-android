package edu.uci.thanote.databases;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.category.CategoryDao;
import edu.uci.thanote.databases.general.DateTimeConverter;

@Database(entities = {Category.class}, version = 1)
@TypeConverters({DateTimeConverter.class})
public abstract class ThanoteDatabase extends RoomDatabase {
    private static ThanoteDatabase instance;
    public abstract CategoryDao categoryDao();

    public static synchronized ThanoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ThanoteDatabase.class,
                    "thanote_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        // this method will only be executed once when the database is created
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CategoryDao categoryDao;

        private PopulateDbAsyncTask(ThanoteDatabase db) {
            categoryDao = db.categoryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // set default category for every user when initializing the category table
            categoryDao.insert(new Category("default"));
            return null;
        }
    }
}
