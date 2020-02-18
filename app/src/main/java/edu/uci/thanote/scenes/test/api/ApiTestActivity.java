package edu.uci.thanote.scenes.test.api;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;
import edu.uci.thanote.scenes.main.MainActivity;
import edu.uci.thanote.scenes.test.BaseActivity;

public class ApiTestActivity extends BaseActivity {
    private static final String[] API_NAMES = {
        ApiList.JOKE.toString(), ApiList.RECIPEPUPPY.toString()
    };

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
        TextListAdapter adapter = new TextListAdapter(API_NAMES, text -> {
            Intent intent = new Intent(ApiTestActivity.this, ApiResultTestActivity.class);
            intent.putExtra(ApiResultTestActivity.EXTRA_APINAME, text);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}

/**
 * AndroidManifest.xml for api test
 <?xml version="1.0" encoding="utf-8"?>
 <manifest xmlns:android="http://schemas.android.com/apk/res/android"
 xmlns:tools="http://schemas.android.com/tools"
 package="edu.uci.thanote">

 <uses-permission android:name="android.permission.INTERNET" />
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

 <application
 android:allowBackup="false"
 android:icon="@mipmap/ic_launcher"
 android:label="@string/app_name"
 android:supportsRtl="true"
 android:theme="@style/AppTheme"
 android:networkSecurityConfig="@xml/network_security_config"
 tools:ignore="GoogleAppIndexingWarning">
 <activity android:name=".scenes.test.api.ApiResultTestActivity" />
 <activity android:name=".scenes.main.MainActivity" />
 <activity android:name=".scenes.test.TestActivity" />
 <activity android:name=".scenes.noteList.NoteListActivity" />
 <activity android:name=".scenes.addnote.AddNoteActivity" />
 <activity android:name=".scenes.addCollection.AddCollectionActivity" />
 <activity
 android:name=".scenes.test.api.ApiTestActivity"
 android:theme="@style/MainTheme">
 <intent-filter>
 <action android:name="android.intent.action.MAIN" />
 <action android:name="android.intent.action.SEARCH" />

 <category android:name="android.intent.category.LAUNCHER" />
 </intent-filter>

 <meta-data
 android:name="android.app.searchable"
 android:resource="@xml/searchable" />
 </activity>
 </application>

 </manifest>
 */