package edu.uci.thanote.scenes.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import edu.uci.thanote.R;
import edu.uci.thanote.helpers.AlarmService;
import edu.uci.thanote.scenes.main.fragments.collection.CollectionFragment;
import edu.uci.thanote.scenes.main.fragments.home.HomeFragment;
import edu.uci.thanote.scenes.main.fragments.setting.SettingFragment;
import edu.uci.thanote.scenes.general.BaseActivity;

public class MainActivity extends BaseActivity {

    private final String TAG = "MainActivity";
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment collectionFragment = new CollectionFragment();
    private final Fragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewModel();
        setupViews();
        startService(new Intent(getApplicationContext(), AlarmService.class));
    }

    private void setupViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.setListener(this::showShortToast);
        viewModel.updateNotificationContent();
    }

    private void setupViews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment;
            final int id = item.getItemId();

            switch (id) {
                case R.id.nav_home:
                    selectedFragment = homeFragment;
                    break;
                case R.id.nav_collection:
                    selectedFragment = collectionFragment;
                    break;
                case R.id.nav_setting:
                    selectedFragment = settingFragment;
                    break;
                default:
                    showShortToast("Unknown item id = " + id);
                    return false;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });
    }
}