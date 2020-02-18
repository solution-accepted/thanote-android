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

    private OnCategoryClickListener listener;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = getItem(position);
        holder.categoryName.setText(category.getName());
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        CategoryHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.category);

            categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnCategoryClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnCategoryClickListener {
        void OnCategoryClick(Category category);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public Category getCategory(int position) {
        return getItem(position);
    }
}
