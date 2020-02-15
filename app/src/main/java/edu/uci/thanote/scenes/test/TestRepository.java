package edu.uci.thanote.scenes.test;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.category.CategoryTable;

import java.util.List;

public class TestRepository {
    private CategoryTable categoryTable;
    private LiveData<List<Category>> categories;

    public TestRepository(Application application) {
        categoryTable = new CategoryTable(application);
        categories = categoryTable.getCategories();
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

    // endregion
}
