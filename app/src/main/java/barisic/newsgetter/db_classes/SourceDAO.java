package barisic.newsgetter.db_classes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

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

    @Query("SELECT * FROM sources_table WHERE domain = :domain LIMIT 1")
    boolean checkSources(String domain);
}
