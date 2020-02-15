package edu.uci.thanote.scenes.note;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.uci.thanote.R;

public class NoteActivity extends AppCompatActivity {
    // TODO: - Declare private UI Components here...
    // private Button xxxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setupViews();
    }

    private void setupViews() {
        // TODO: - Binding Views Here...
        // xxxButton = findViewById(R.id.button_xxx);
    }
}
