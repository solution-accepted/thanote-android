package edu.uci.thanote.scenes.main.fragments.home;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import edu.uci.thanote.R;
import edu.uci.thanote.databases.note.Note;

public class HomeRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    private final String TAG = "HomeRecyclerViewViewHolder";

    private TextView textViewTitle;
    private TextView textViewDate;
    private TextView textViewDescription;
    private ImageView imageViewThumbnailLarge;
    private ImageView imageViewThumbnailSmall;

    private ImageButton buttonShare;
    private ImageButton buttonFavorite;

    public HomeRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewTitle = itemView.findViewById(R.id.text_view_note_title);
        textViewDate = itemView.findViewById(R.id.text_view_note_date);
        textViewDescription = itemView.findViewById(R.id.text_view_note_description);
        imageViewThumbnailLarge = itemView.findViewById(R.id.image_view_note_thumbnail_large);
        imageViewThumbnailSmall = itemView.findViewById(R.id.image_view_note_thumbnail_small);

        buttonShare = itemView.findViewById(R.id.button_note_share);
        buttonFavorite = itemView.findViewById(R.id.button_note_favorite);
    }

    public void setNote(Note note) {
        textViewTitle.setText(note.getTitle());
        textViewDate.setText("");
        textViewDescription.setText(note.getDetail());

        String url = note.getImageUrl();
        if (!url.isEmpty()) {
            Glide.with(itemView)
                    .load(url)
                    .transform(new RoundedCorners(10))
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            final int height = resource.getIntrinsicHeight();
                            final int width = resource.getIntrinsicWidth();
                            final ImageView imageView = height < 200 || width < 200 ? imageViewThumbnailSmall : imageViewThumbnailLarge;
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            Log.i(TAG, "onLoadCleared: ");
                        }
                    });
        }

        buttonShare.setOnClickListener(v -> listener.onButtonShareClicked(note));
        buttonFavorite.setOnClickListener(v -> listener.onButtonFavoriteClicked(note));
    }

    public interface Listener {
        void onButtonShareClicked(Note note);

        void onButtonFavoriteClicked(Note note);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
