package edu.uci.thanote.scenes.main.fragments.collection;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.category.CategoryTable;

public class CollectionRepository {
    private CollectionRepositoryListener listener;
    // database tables
    private CategoryTable categoryTable;
    // data fields
    private LiveData<List<Category>> categories;

    public CollectionRepository(Application application) {
        categoryTable = new CategoryTable(application);
        categories = categoryTable.getCategories();
    }

    public void insert(Category category) {
        categoryTable.insert(category);
    }

    public void update(Category category) {
        categoryTable.update(category);
    }

    public void delete(Category category) {
        categoryTable.delete(category);
    }

    public void deleteAllCategories() {
        categoryTable.deleteAllCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public interface CollectionRepositoryListener {
        void onCategoryClick(Category category);
    }
    public void setListener(CollectionRepository.CollectionRepositoryListener listener) {
        this.listener = listener;
    }

}
