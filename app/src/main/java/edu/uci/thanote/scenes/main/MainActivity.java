package edu.uci.thanote.scenes.main;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.uci.thanote.R;
import edu.uci.thanote.helpers.AlarmService;
import edu.uci.thanote.scenes.main.fragments.home.HomeFragment;
import edu.uci.thanote.scenes.main.fragments.collection.CollectionFragment;
import edu.uci.thanote.scenes.main.fragments.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import edu.uci.thanote.scenes.test.BaseActivity;

public class MainActivity extends BaseActivity {

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
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_collection:
                    selectedFragment = new CollectionFragment();
                    break;
                case R.id.nav_setting:
                    selectedFragment = new SettingFragment();
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