package edu.uci.thanote.scenes.addCollection;

import android.app.Application;

import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.category.CategoryTable;

public class AddCollectionRepository {
    private CategoryTable categoryTable;

    public AddCollectionRepository(Application application) {
        categoryTable = new CategoryTable(application);
    }

    public void insert(Category category) {
        categoryTable.insert(category);
    }

    public void update(Category category) {
        categoryTable.update(category);
    }

}
