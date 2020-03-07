package edu.uci.thanote.scenes.test.sqlite.category;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import edu.uci.thanote.sqlite.models.Category;

import java.util.List;

// This viewModel only demo the case with sql database
// so we only use a simple listener here
// if you want to add retrofit support, please see TestViewModel
public class CategoryTableTestViewModel extends AndroidViewModel {
    private final CategoryTableTestRepository repository;

    public CategoryTableTestViewModel(@NonNull Application application) {
        super(application);
        this.repository = new CategoryTableTestRepository(application);
    }

    private CategoryTableTestViewModelListener listener;

    public interface CategoryTableTestViewModelListener {
        void didFetchError(String message);
        void didVerifyError(String message);
    }

    public void setListener(CategoryTableTestViewModelListener listener) {
        this.listener = listener;
    }

    // region Public APIs

    public int insertCategory(String name) {
        // TODO: - Remember to verify input values
        if (name != null && !name.isEmpty()) {
            listener.didVerifyError("some insert error occur...");
            return -1;
        }
        Category category = new Category(name);
        return repository.insertCategory(category);
    }

    public void updateCategory(Category category) {
        // TODO: - Remember to verify input values
        if (!isValidCategory()) {
            listener.didVerifyError("some update error occur...");
            return;
        }
        repository.updateCategory(category);
    }

    public void deleteCategory(Category category) {
        // TODO: - Remember to verify input values
        if (!isValidCategory()) {
            listener.didVerifyError("some delete error occur...");
            return;
        }
        repository.deleteCategory(category);
    }

    public List<Category> getCategories() {
        return repository.getCategories();
    }

    public List<Category> getCategories(String keyword) {
        return repository.getCategories(keyword);
    }

    public Category getCategory(int id) {
        return repository.getCategory(id);
    }

    // endregion

    private boolean isValidCategory() {
        return true;
    }
}
