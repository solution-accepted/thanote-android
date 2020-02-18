package edu.uci.thanote.scenes.test;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.uci.thanote.R;
import edu.uci.thanote.apis.demo.Post;
import edu.uci.thanote.apis.joke.SingleJoke;
import edu.uci.thanote.apis.joke.TwoPartJoke;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.general.DateTimeConverter;
import edu.uci.thanote.databases.note.Note;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    private TextView testTextView;
    private Button insertButton;
    private Button updateButton;
    private Button deleteButton;
    private Button deleteAllButton;
    private Button singleJokeButton;
    private Button twopartJokeButton;
    private Button postsButton;
    private Button searchSingleJokeButton;
    private Button searchTwoPartJokeButton;

    private TestViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setupViewModel();
        setupViews();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(TestViewModel.class);
        viewModel.setListener(new TestViewModel.TestViewModelListener() {
            @Override
            public void didFetchSingleJoke(SingleJoke joke) {
                testTextView.setText(joke.getJoke());
                showToast("get a single joke");

                // test insert note
                String title = "A " + joke.getCategory() + " joke";
                String detail = joke.getJoke();
                Note note = new Note(title, detail, 1, "");
                viewModel.insertNote(note);
            }

            @Override
            public void didFetchTwoPartJoke(TwoPartJoke joke) {
                String jokeString = joke.getSetup() + "\n" + joke.getDelivery();
                testTextView.setText(jokeString);
                showToast("get a two part joke");

                // test insert note
                String title = "A " + joke.getCategory() + " joke";
                String detail = joke.getSetup() + "\n\n" + joke.getDelivery();
                Note note = new Note(title, detail, 1, "");
                viewModel.insertNote(note);
            }

            @Override
            public void didFetchSingleJokeByKey(SingleJoke joke) {
                testTextView.setText(joke.getJoke());
                showToast("get a single joke");
            }

            @Override
            public void didFetchTwoPartJokeByKey(TwoPartJoke joke) {
                String jokeString = joke.getSetup() + "\n" + joke.getDelivery();
                testTextView.setText(jokeString);
                showToast("get a two part joke");
            }

            @Override
            public void didFetchError(String message) {
                showToast(message);
            }

            @Override
            public void didVerifyError(String message) {
                showToast(message);
            }

            @Override
            public void didFetchAllPosts(List<Post> posts) {
                String content = "";

                for (Post post : posts) {
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";
                }

                testTextView.setText(content);
                showToast("refresh posts");
            }
        });
        viewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                StringBuilder builder = new StringBuilder();

                for (Category category : categories) {
                    String dateString = DateTimeConverter.dateToString(category.getCreateDate());
                    builder.append("id: ").append(category.getId()).append(", name: ").append(category.getName()).append(", created: ").append(dateString).append("\n");
                }
                testTextView.setText(builder.toString());
                showToast("refresh category");
            }
        });

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                StringBuilder builder = new StringBuilder();

                for (Note note : notes) {
                    String id = String.valueOf(note.getId());
                    String categoryId = String.valueOf(note.getCategoryId());
                    String title = note.getTitle();
                    String detail = note.getDetail();
                    String imageUrl = note.getImageUrl();
                    String createDate = DateTimeConverter.dateToString(note.getCreateDate());

                    builder.append("id: " + id + ", ");
                    builder.append("categoryId: " + categoryId + ", ");
                    builder.append("title: " + title + ", ");
                    builder.append("detail: " + detail + ", ");
                    builder.append("imageUrl: " + imageUrl + ", ");
                    builder.append("createDate: " + createDate + "\n");
                }
                testTextView.setText(builder.toString());
                showToast("refresh note");
            }
        });
    }

    private void setupViews() {
        testTextView = findViewById(R.id.text_view_test);
        insertButton = findViewById(R.id.button_insert);
        updateButton = findViewById(R.id.button_update);
        deleteButton = findViewById(R.id.button_delete);
        deleteAllButton = findViewById(R.id.button_delete_all);
        singleJokeButton = findViewById(R.id.button_get_single_joke);
        twopartJokeButton = findViewById(R.id.button_get_two_part_joke);
        searchSingleJokeButton = findViewById(R.id.button_search_single_joke);
        searchTwoPartJokeButton = findViewById(R.id.button_search_two_part_joke);
        postsButton = findViewById(R.id.button_get_posts);
        insertButton.setOnClickListener(onClickListener);
        updateButton.setOnClickListener(onClickListener);
        deleteButton.setOnClickListener(onClickListener);
        deleteAllButton.setOnClickListener(onClickListener);
        singleJokeButton.setOnClickListener(onClickListener);
        twopartJokeButton.setOnClickListener(onClickListener);
        postsButton.setOnClickListener(onClickListener);
        searchSingleJokeButton.setOnClickListener(onClickListener);
        searchTwoPartJokeButton.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_insert:
                    viewModel.testInsert();
                    break;
                case R.id.button_update:
                    viewModel.testUpdate();
                    break;
                case R.id.button_delete:
                    viewModel.testDelete();
                    break;
                case R.id.button_delete_all:
                    viewModel.testDeleteAll();
                    break;
                case R.id.button_get_single_joke:
                    viewModel.getSingleJoke();
                    break;
                case R.id.button_get_two_part_joke:
                    viewModel.getTwoPartJoke();
                    break;
                case R.id.button_get_posts:
                    viewModel.getAllPosts();
                    break;
                case R.id.button_search_single_joke:
                    viewModel.searchSingleJoke("why");
                    break;
                case R.id.button_search_two_part_joke:
                    viewModel.searchTwoPartJoke("why");
                    break;
                default:
                    showToast("Error test with view: " + view.getId());
                    break;
            }
        }
    };

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
