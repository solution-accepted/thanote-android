package edu.uci.thanote.scenes.addCollection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import edu.uci.thanote.databases.category.Category;

public class AddCollectionViewModel extends AndroidViewModel {

    private final AddCollectionRepository repository;

    public AddCollectionViewModel(@NonNull Application application) {
        super(application);
        repository = new AddCollectionRepository(application);
    }

    public void insert(Category category) {
        repository.insert(category);
    }

    // todo add update category
    public void update(Category category) {
        repository.update(category);
    }

}
