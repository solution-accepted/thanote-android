package edu.uci.thanote.scenes.main.fragments.collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.uci.thanote.R;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.scenes.addCollection.AddCollectionActivity;
import edu.uci.thanote.scenes.general.BaseFragment;
import edu.uci.thanote.scenes.noteList.NoteListActivity;

public class CollectionFragment extends BaseFragment {

    private final String DEFAULT_DELETE_WARNING = "default could not be deleted";

    private RecyclerView recyclerView;
    private CollectionViewModel viewModel;
    private final CollectionAdapter adapter = new CollectionAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        setupViewModel();
        setupViews(view);
        return view;
    }

    public void addCategory(View view) {
        FloatingActionButton buttonAddNote = view.findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(AddCollectionActivity.class);

            }
        });
    }

    public void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CollectionViewModel.class);

        viewModel.getCategories().observe(getActivity(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                adapter.submitList(categories);
            }
        });

        adapter.setOnCategoryClickListener(new CollectionAdapter.CollectionAdapterOnClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                openNoteList(category);
            }
        });
    }

    private void setupViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_collection);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        addCategory(view);
        createDeleteView();
    }

    private void openNoteList(Category category) {
        Intent intent = new Intent(getActivity(), NoteListActivity.class);
        intent.putExtra(NoteListActivity.CATEGORY_ID, category.getId());
        startActivity(intent);
    }

    public void createDeleteView() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Category category = adapter.getCategory(viewHolder.getAdapterPosition());
                if (category.getId() == Category.DEFAULT_CATEGORY_ID) {
                    showShortToast(DEFAULT_DELETE_WARNING);
                    adapter.notifyDataSetChanged();
                } else {
                    viewModel.delete(category);
                }
            }
        }).attachToRecyclerView(recyclerView);
    }
}