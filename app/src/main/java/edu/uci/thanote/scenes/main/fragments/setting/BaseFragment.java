package edu.uci.thanote.scenes.main.fragments.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.Serializable;

public class BaseFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public void showShortToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void openPage(Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    public void openPage(Class<?> cls, String key, Serializable object) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, object);
        intent.putExtras(bundle);
    }
}
