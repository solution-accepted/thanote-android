package edu.uci.thanote.databases.category;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.databases.ThanoteDatabase;

import java.util.List;

public class CategoryTable {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> categories;

    public CategoryTable(Application application) {
        ThanoteDatabase database = ThanoteDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        categories = categoryDao.getCategories();
    }

    // region Public APIs

    public void insert(Category category) {
        new InsertCategoryAsyncTask(categoryDao).execute(category);
    }

    public void update(Category category) {
        new UpdateCategoryAsyncTask(categoryDao).execute(category);
    }

    public void delete(Category category) {
        new DeleteCategoryAsyncTask(categoryDao).execute(category);
    }

    public void deleteAllCategories() {
        new DeleteAllCategoriesAsyncTask(categoryDao).execute();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    // endregion

    // region AsyncTask Functions

    private static class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        private InsertCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            // we set constraints for column,
            // if insert the same object, we update it
            try {
                categoryDao.insert(categories[0]);
            } catch (SQLiteConstraintException e) {
                categoryDao.update(categories[0]);
            }

            return null;
        }
    }

    private static class UpdateCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        private UpdateCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.update(categories[0]);
            return null;
        }
    }

    private static class DeleteCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        private DeleteCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.delete(categories[0]);
            return null;
        }
    }

    private static class DeleteAllCategoriesAsyncTask extends AsyncTask<Void, Void, Void> {
        private CategoryDao categoryDao;

        private DeleteAllCategoriesAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            categoryDao.deleteAllCategories();
            return null;
        }
    }

    // endregion
}
