package edu.uci.thanote.scenes.main.fragments.collection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.uci.thanote.databases.category.Category;

public class CollectionViewModel extends AndroidViewModel {
    private final CollectionRepository repository;
    private LiveData<List<Category>> categories;
    private CollectionViewModelListener listener;

    public interface CollectionViewModelListener {
        void onCategoryClick(Category category);
    }

    public CollectionViewModel(@NonNull Application application) {
        super(application);
        repository = new CollectionRepository(application);
        repository.setListener(collectionRepositoryListener);
        categories = repository.getCategories();
    }

    public void insert(Category category) {
        repository.insert(category);
    }

    public void update(Category category) {
        repository.update(category);
    }

    public void delete(Category category) {
        repository.delete(category);
    }

    public void deleteAllCategories() {
        repository.deleteAllCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void setListener(CollectionViewModel.CollectionViewModelListener listener) {
        this.listener = listener;
    }

    private CollectionRepository.CollectionRepositoryListener collectionRepositoryListener = new CollectionRepository.CollectionRepositoryListener() {
        @Override
        public void onCategoryClick(Category category) {
            // todo

        }
    };

}
