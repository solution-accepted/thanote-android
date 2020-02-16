package edu.uci.thanote.databases.note;

import androidx.room.Entity;
import edu.uci.thanote.databases.general.BaseTable;

@Entity(tableName = "note_table")
public class Note extends BaseTable {
    // Column Infos
    private String title;
    private String detail;
    private int categoryId;
    private String imageUrl;

    public Note(String title, String detail, int categoryId, String imageUrl) {
        super();
        this.title = title;
        this.detail = detail;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
