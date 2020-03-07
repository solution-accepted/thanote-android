package edu.uci.thanote.scenes.test.sqlite;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.scenes.main.fragments.setting.ImageTextItem;
import edu.uci.thanote.scenes.main.fragments.setting.ImageTextListAdapter;
import edu.uci.thanote.scenes.test.BaseActivity;
import edu.uci.thanote.scenes.test.sqlite.category.CategoryTableTestActivity;

public class SQLiteTestActivity extends BaseActivity {
    private static final String CATEGORY_TABLE = "Category Table";
    private static final String NOTE_TABLE = "Note Table";
    private static final ImageTextItem[] TABLES = new ImageTextItem[] {
        new ImageTextItem(R.drawable.ic_default, CATEGORY_TABLE),
        new ImageTextItem(R.drawable.ic_default, NOTE_TABLE),
    }
;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_test);
        setupViews();
    }

    private void setupViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_sqlite_test);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageTextListAdapter adapter = new ImageTextListAdapter(TABLES, item -> {
            switch (item.getText()) {
                case CATEGORY_TABLE:
                    openPage(CategoryTableTestActivity.class);
                    break;
                case NOTE_TABLE:
                    showShortToast(item.getText());
                    break;
                default:
                    showShortToast("Error command: " + item.getText());
                    break;
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
