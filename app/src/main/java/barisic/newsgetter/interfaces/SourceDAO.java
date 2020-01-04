package barisic.newsgetter.interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import barisic.newsgetter.db_classes.Source;

@Dao
public interface SourceDAO {

    @Insert
    void insertSource(Source source);

    @Update
    void updateSource(Source source);

    @Delete
    void deleteSource(Source source);

    @Query("DELETE FROM sources_table")
    void deleteAll();

    @Query("SELECT * FROM sources_table ORDER BY id DESC")
    LiveData<List<Source>> getSourcesUpdate();

    @Query("SELECT * FROM sources_table ORDER BY id DESC")
    List<Source> getAllSources();
}
