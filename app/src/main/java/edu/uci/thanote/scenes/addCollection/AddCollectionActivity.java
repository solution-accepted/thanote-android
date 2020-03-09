package edu.uci.thanote.scenes.addCollection;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import edu.uci.thanote.R;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.scenes.test.BaseActivity;

public class AddCollectionActivity extends BaseActivity {

    private EditText newCategoryEditText;
    private AddCollectionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        setupViewModel();
        setupViews();
    }

    public void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AddCollectionViewModel.class);
    }

    public void setupViews() {
        newCategoryEditText = findViewById(R.id.edit_text_new_cagetory);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    private void saveCategory() {
        String categoryName = newCategoryEditText.getText().toString();

        if (categoryName.trim().isEmpty()) {
            showShortToast("Please insert category name");
            return;
        }

        Category category = new Category(categoryName);
        viewModel.insert(category);

        showShortToast("Category saved");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveCategory();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
