package edu.uci.thanote.scenes.main.fragments.setting;

import edu.uci.thanote.R;

public class ImageTextItem {
    private int imageRescourceId;
    private String text;
    private boolean isShowImage; // default is true

    public ImageTextItem(int imageRescourceId, String text, boolean isShowImage) {
        this.imageRescourceId = imageRescourceId;
        this.text = text;
        this.isShowImage = isShowImage;
    }

    public ImageTextItem(int imageRescourceId, String text) {
        this.imageRescourceId = imageRescourceId;
        this.text = text;
        this.isShowImage = true;
    }

    public ImageTextItem(String text, boolean isShowImage) {
        this.imageRescourceId = R.drawable.ic_default;
        this.text = text;
        this.isShowImage = isShowImage;
    }

    public ImageTextItem(String text) {
        this.imageRescourceId = R.drawable.ic_default;
        this.text = text;
        this.isShowImage = true;
    }

    public int getImageRescourceId() {
        return imageRescourceId;
    }

    public String getText() {
        return text;
    }

    public boolean isShowImage() {
        return isShowImage;
    }
}
