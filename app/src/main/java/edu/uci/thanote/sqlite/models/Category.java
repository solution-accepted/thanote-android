package edu.uci.thanote.sqlite.models;

/**
 * Category is a table that contains category fields
 * <p><h3>When initiating the database, the default category will be auto-created with id {@link Category#DEFAULT_CATEGORY_ID} and name {@link Category#DEFAULT_CATEGORY_NAME}</h3></p>
 * <p>1. id: Primary Key, auto increased</p>
 * <p>2. createDate: Date when this object is created</p>
 * <p>3. name: category's name, Unique</p>
 * @see BaseModel
 */
public class Category extends BaseModel {
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

    @Override
    public String toString() {
        return super.toString() + "Category{" +
                "name='" + name + '\'' +
                '}';
    }
}
