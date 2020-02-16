package edu.uci.thanote.databases.category;

import androidx.room.Entity;
import androidx.room.Index;
import edu.uci.thanote.databases.general.BaseTable;

@Entity(tableName = "category_table", indices = {@Index(value = "name", unique = true)})
public class Category extends BaseTable {
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
