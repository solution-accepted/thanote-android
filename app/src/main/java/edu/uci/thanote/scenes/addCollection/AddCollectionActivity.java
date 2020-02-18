package edu.uci.thanote.scenes.addCollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.uci.thanote.R;

public class AddCollectionActivity extends AppCompatActivity {
    public static final String EXTRA_CAGETORY =
            "com.example.myapplication.EXTRA_CAGETORY";

    private EditText newCategoryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        setupViews();
    }

    public void setupViews() {
        newCategoryEditText = findViewById(R.id.new_cagetory);

    }

    private void saveCategory() {
        String categoryName = newCategoryEditText.getText().toString();

        if (categoryName.trim().isEmpty()) {
            Toast.makeText(this, "Please insert category name", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_CAGETORY, categoryName);

        setResult(RESULT_OK, data);
        finish();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
