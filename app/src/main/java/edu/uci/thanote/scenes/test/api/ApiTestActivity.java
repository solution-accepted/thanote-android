package edu.uci.thanote.scenes.test.api;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.scenes.test.BaseActivity;

public class ApiTestActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);
        setupViews();
    }

    private void setupViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_api_test);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextListAdapter adapter = new TextListAdapter(ApiList.getAllApiNames(), text -> {
            Intent intent = new Intent(ApiTestActivity.this, ApiResultTestActivity.class);
            intent.putExtra(ApiResultTestActivity.EXTRA_APINAME, text);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}