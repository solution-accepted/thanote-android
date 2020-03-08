package edu.uci.thanote.scenes.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.uci.thanote.R;
import edu.uci.thanote.scenes.main.fragments.setting.ImageTextItem;
import edu.uci.thanote.scenes.main.fragments.setting.ImageTextListAdapter;

public class ApiSpinnerAdapter extends ArrayAdapter<ImageTextItem> {
    private ImageTextItem[] items;

    public ApiSpinnerAdapter(@NonNull Context context, @NonNull ImageTextItem[] items) {
        super(context, 0, items);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_text, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.text_view_image_text);
        ImageTextItem item = getItem(position);

        if (item != null) {
            textView.setText(item.getText());

            if (item.isShowImage()) {
                textView.setCompoundDrawablesWithIntrinsicBounds(item.getImageRescourceId(), 0, 0, 0);
            }
        }

        return convertView;
    }
}
