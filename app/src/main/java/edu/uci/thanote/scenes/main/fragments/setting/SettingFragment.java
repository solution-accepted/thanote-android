package edu.uci.thanote.scenes.main.fragments.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.scenes.notification.NotificationActivity;
import edu.uci.thanote.scenes.test.api.ApiTestActivity;
import edu.uci.thanote.scenes.test.sqlite.SQLiteTestActivity;

public class SettingFragment extends BaseFragment {
    private static final String SETTING_ACTION_NOTIFICATIONS = "Notifications";
    private static final String SETTING_ACTION_BUG_REPORT = "Bug Report";
    private static final String SETTING_ACTION_API_Test = "Api Test";
    private static final String SETTING_ACTION_SQLITE_Test = "SQLite Test";
    private static final ImageTextItem[] SETTING_ITEMS = {
        new ImageTextItem(R.drawable.ic_notifications, SETTING_ACTION_NOTIFICATIONS),
        new ImageTextItem(R.drawable.ic_bug_report, SETTING_ACTION_BUG_REPORT),
        new ImageTextItem(R.drawable.ic_api_test, SETTING_ACTION_API_Test),
        new ImageTextItem(R.drawable.ic_default, SETTING_ACTION_SQLITE_Test)
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_setting_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ImageTextListAdapter adapter = new ImageTextListAdapter(SETTING_ITEMS, imageTextListAdapterListener);
        recyclerView.setAdapter(adapter);
    }

    private ImageTextListAdapter.ImageTextListAdapterListener imageTextListAdapterListener = item -> {
        switch (item.getText()) {
            case SETTING_ACTION_NOTIFICATIONS:
                openPage(NotificationActivity.class);
                break;
            case SETTING_ACTION_BUG_REPORT:
                showShortToast(SETTING_ACTION_BUG_REPORT);
                break;
            case SETTING_ACTION_API_Test:
                openPage(ApiTestActivity.class);
                break;
            case SETTING_ACTION_SQLITE_Test:
                openPage(SQLiteTestActivity.class);
                break;
            default:
                showShortToast("Error action: " + item.getText());
                break;
        }
    };
}
