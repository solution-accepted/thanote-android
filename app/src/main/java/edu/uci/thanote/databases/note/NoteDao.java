package edu.uci.thanote.databases.note;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM note_table WHERE categoryId = :categoryId")
    LiveData<List<Note>> getNotes(int categoryId);
}
