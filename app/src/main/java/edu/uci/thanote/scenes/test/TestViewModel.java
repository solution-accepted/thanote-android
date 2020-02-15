package edu.uci.thanote.scenes.test;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import edu.uci.thanote.databases.category.Category;

import java.util.List;
import java.util.Random;

public class TestViewModel extends AndroidViewModel {
    private final TestRepository repository;
    private LiveData<List<Category>> categories;

    public TestViewModel(@NonNull Application application) {
        super(application);
        repository = new TestRepository(application);
        categories = repository.getCategories();
    }

    // region Public APIs

    public void insertCategory(Category category) {
        if (!isValidCategory(category)) {
            listener.didVerifyError("some insert error occur...");
            return;
        }
        repository.insertCategory(category);
    }

    public void updateCategory(Category category) {
        if (!isValidCategory(category)) {
            listener.didVerifyError("some update error occur...");
            return;
        }
        repository.updateCategory(category);
    }

    public void deleteCategory(Category category) {
        if (!isValidCategory(category)) {
            listener.didVerifyError("some delete error occur...");
            return;
        }
        repository.deleteCategory(category);
    }

    public void deleteAllCategories() {
        repository.deleteAllCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    // endregion

    // TODO: - Must define listener to notify view change state or messages
    // region ViewModel Listener

    private TestViewModelListener listener;

    public interface TestViewModelListener {
        void didVerifyError(String message);
    }

    public void setListener(TestViewModelListener listener) {
        this.listener = listener;
    }

    // endregion

    // TODO: - Must implement some verify method before processing data to DB
    private boolean isValidCategory(Category category) {
        return true;
    }

    // TODO: - Only for test, will delete later...
    // region Test

    public static final int CATEGORY_TABLE = 0;
    public static final int NOTE_TABLE = 1;
    private final int testTable = CATEGORY_TABLE;

    public void testInsert() {
        if (testTable == CATEGORY_TABLE) {
            Category category = new Category("Test " + (int) (Math.random() * 100));
            insertCategory(category);
        } else {

        }
    }

    public void testUpdate() {
        if (testTable == CATEGORY_TABLE) {
            Category category = new Category("Nice");
            category.setId(2);
            updateCategory(category);
        } else {

        }
    }

    public void testDelete() {
        if (testTable == CATEGORY_TABLE) {
            Category category = new Category("Nice");
            category.setId(2);
            deleteCategory(category);
        } else {

        }
    }

    public void testDeleteAll() {
        if (testTable == CATEGORY_TABLE) {
            deleteAllCategories();
        } else {

        }
    }

    // endregion
}
