package edu.uci.thanote.scenes.test.api;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uci.thanote.R;

public class TextListAdapter extends RecyclerView.Adapter<TextListAdapter.ViewHolder> {
    public interface TextListAdapterListener {
        void onItemClick(String text);
    }

    private String[] items;
    private TextListAdapterListener listener;

    TextListAdapter(String[] items, TextListAdapterListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = items[position];
        holder.bind(text, listener);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.category);
        }

        public void bind(final String text, final TextListAdapterListener listener) {
            textView.setText(text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(text);
                }
            });
        }
    }
}
