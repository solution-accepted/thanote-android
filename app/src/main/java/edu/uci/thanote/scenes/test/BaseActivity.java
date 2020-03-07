package edu.uci.thanote.scenes.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class BaseActivity extends AppCompatActivity {
    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void openPage(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void openPage(Class<?> cls, String key, Serializable object) {
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, object);
        intent.putExtras(bundle);
    }
}
