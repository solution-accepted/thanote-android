package edu.uci.thanote.scenes.main.fragments.collection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import edu.uci.thanote.R;
import edu.uci.thanote.databases.category.Category;

public class CollectionAdapter extends ListAdapter<Category, CollectionAdapter.CategoryHolder> {

    private CollectionAdapterOnClickListener listener;

    CollectionAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = getItem(position);
        holder.categoryNameTextView.setText(category.getName());
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView categoryNameTextView;

        CategoryHolder(View view) {
            super(view);
            categoryNameTextView = view.findViewById(R.id.category);

            categoryNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onCategoryClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface CollectionAdapterOnClickListener {
        void onCategoryClick(Category category);
    }

    public void setOnCategoryClickListener(CollectionAdapterOnClickListener listener) {
        this.listener = listener;
    }

    public Category getCategory(int position) {
        return getItem(position);
    }
}
