package edu.uci.thanote.databases.note;

import androidx.room.Entity;
import edu.uci.thanote.databases.category.Category;
import edu.uci.thanote.databases.general.BaseTable;

/**
 * Note is a table that contains note fields
 * <p>1. id: Primary Key, auto increased</p>
 * <p>2. createDate: Date when this object is created</p>
 * <p>3. title: note's title, not null (should set empty is no value)</p>
 * <p>4. detail: note's detail, not null (should set empty is no value)</p>
 * <p>5. categoryId: mapping note with category by using category id (default = {@link Category#DEFAULT_CATEGORY_ID})</p>
 * <p>6. imageUrl: note's image url, not null (should set empty is no value)</p>
 * @see BaseTable
 */
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
