package edu.uci.thanote.scenes.test.sqlite.category;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;
import edu.uci.thanote.R;
import edu.uci.thanote.scenes.test.BaseActivity;

public class CategoryTableTestActivity extends BaseActivity {
    private EditText insertNameEditText;
    private EditText updateIdEditText;
    private EditText updateNameEditText;
    private EditText deleteIdEditText;
    private EditText queryIdEditText;
    private EditText queryNameEditText;
    private Button insertButton;
    private Button updateButton;
    private Button deleteButton;
    private Button queryIdButton;
    private Button queryNameButton;
    private Button queryAllButton;
    private TextView resultTextView;

    private CategoryTableTestViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_table_test);
        setupViewModel();
        setupViews();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CategoryTableTestViewModel.class);
        viewModel.setListener(new CategoryTableTestViewModel.CategoryTableTestViewModelListener() {
            @Override
            public void didFetchError(String message) {
                showShortToast(message);
            }

            @Override
            public void didVerifyError(String message) {
                showShortToast(message);
            }
        });
    }

    private void setupViews() {
        insertNameEditText = findViewById(R.id.edit_text_insert_name);
        updateIdEditText = findViewById(R.id.edit_text_update_id);
        updateNameEditText = findViewById(R.id.edit_text_update_name);
        deleteIdEditText = findViewById(R.id.edit_text_delete_id);
        queryIdEditText = findViewById(R.id.edit_text_query_id);
        queryNameEditText = findViewById(R.id.edit_text_query_name);

        insertButton = findViewById(R.id.button_insert_category);
        insertButton.setOnClickListener(viewOnClickListener);
        updateButton = findViewById(R.id.button_update_category);
        updateButton.setOnClickListener(viewOnClickListener);
        deleteButton = findViewById(R.id.button_delete_category);
        deleteButton.setOnClickListener(viewOnClickListener);
        queryIdButton = findViewById(R.id.button_query_category_id);
        queryIdButton.setOnClickListener(viewOnClickListener);
        queryNameButton = findViewById(R.id.button_query_category_name);
        queryNameButton.setOnClickListener(viewOnClickListener);
        queryAllButton = findViewById(R.id.button_query_category_all);
        queryAllButton.setOnClickListener(viewOnClickListener);

        resultTextView = findViewById(R.id.text_view_category_result);
    }

    // TODO: - Not finished yet...
    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_insert_category:
                    viewModel.insertCategory(insertNameEditText.getText().toString());
                    break;
                case R.id.button_update_category:

                    break;

                default:
                    showShortToast("Unknown operation for id:" + v.getId());
                    break;
            }
        }
    };
}
