package edu.uci.thanote.databases.category;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    // we keep the default category here
    @Query("DELETE FROM category_table WHERE name != 'default'")
    void deleteAllCategories();

    @Query("SELECT * FROM category_table")
    LiveData<List<Category>> getCategories();
}
