package edu.uci.thanote.scenes.test;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.uci.thanote.R;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.general.DateTimeConverter;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    private TextView testTextView;
    private Button insertButton;
    private Button updateButton;
    private Button deleteButton;
    private Button deleteAllButton;

    private TestViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        viewModel = new ViewModelProvider(this).get(TestViewModel.class);
        viewModel.setListener(new TestViewModel.TestViewModelListener() {
            @Override
            public void didVerifyError(String message) {
                showToast(message);
            }
        });
        viewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                StringBuilder builder = new StringBuilder();

                for (Category category : categories) {
                    String dateString = DateTimeConverter.dateToString(category.getCreateDate());
                    builder.append("id: ").append(category.getId()).append(", name: ").append(category.getName()).append(", created: ").append(dateString).append("\n");
                }
                testTextView.setText(builder.toString());
                showToast("refresh");
            }
        });

        setupViews();
    }

    private void setupViews() {
        testTextView = findViewById(R.id.text_view_test);
        insertButton = findViewById(R.id.button_insert);
        updateButton = findViewById(R.id.button_update);
        deleteButton = findViewById(R.id.button_delete);
        deleteAllButton = findViewById(R.id.button_delete_all);
        insertButton.setOnClickListener(onClickListener);
        updateButton.setOnClickListener(onClickListener);
        deleteButton.setOnClickListener(onClickListener);
        deleteAllButton.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_insert:
                    viewModel.testInsert();
                    break;
                case R.id.button_update:
                    viewModel.testUpdate();
                    break;
                case R.id.button_delete:
                    viewModel.testDelete();
                    break;
                case R.id.button_delete_all:
                    viewModel.testDeleteAll();
                    break;
                default:
                    showToast("Error test with view: " + view.getId());
                    break;
            }
        }
    };

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
