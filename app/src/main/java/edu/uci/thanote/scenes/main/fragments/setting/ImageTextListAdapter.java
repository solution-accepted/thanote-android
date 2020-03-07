package edu.uci.thanote.scenes.main.fragments.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;

public class ImageTextListAdapter extends RecyclerView.Adapter<ImageTextListAdapter.ViewHolder> {
    public interface ImageTextListAdapterListener {
        void onItemClick(ImageTextItem item);
    }

    private ImageTextItem[] items;
    private ImageTextListAdapterListener listener;

    public ImageTextListAdapter(ImageTextItem[] items, ImageTextListAdapterListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageTextListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_text, parent, false);
        return new ImageTextListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageTextListAdapter.ViewHolder holder, int position) {
        ImageTextItem item = items[position];
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text_view_image_text);
        }

        public void bind(final ImageTextItem item, final ImageTextListAdapter.ImageTextListAdapterListener listener) {
            textView.setText(item.getText());
            if (item.isShowImage()) {
                textView.setCompoundDrawablesWithIntrinsicBounds(item.getImageRescourceId(), 0, 0, 0);
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
