package edu.uci.thanote.databases.category;

import androidx.room.Entity;
import androidx.room.Index;
import edu.uci.thanote.databases.general.BaseTable;

/**
 * Category is a table that contains category fields
 * <p><h3>When initiating the database, the default category will be auto-created with id {@link Category#DEFAULT_CATEGORY_ID} and name {@link Category#DEFAULT_CATEGORY_NAME}</h3></p>
 * <p>1. id: Primary Key, auto increased</p>
 * <p>2. createDate: Date when this object is created</p>
 * <p>3. name: category's name, Unique</p>
 * @see BaseTable
 */
@Entity(tableName = "category_table", indices = {@Index(value = "name", unique = true)})
public class Category extends BaseTable {
    public static final int DEFAULT_CATEGORY_ID = 1;
    public static final String DEFAULT_CATEGORY_NAME = "default";

    // Column Info
    private String name;

    public Category(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
