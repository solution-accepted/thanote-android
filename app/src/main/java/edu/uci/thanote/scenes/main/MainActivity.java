package edu.uci.thanote.scenes.main;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import edu.uci.thanote.R;

import edu.uci.thanote.scenes.main.fragments.home.HomeFragment;

import edu.uci.thanote.scenes.main.fragments.collection.CollectionFragment;
import edu.uci.thanote.scenes.main.fragments.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment collectionFragment = new CollectionFragment();
    private final Fragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
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
                    Log.e(TAG, "Unknown item id = " + id);
                    return false;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });
    }
}