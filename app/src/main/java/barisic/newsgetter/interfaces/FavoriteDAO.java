package barisic.newsgetter.interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import barisic.newsgetter.db_classes.Favorite;

@Dao
public interface FavoriteDAO {

    @Insert
    void insertFavorite(Favorite favorite);

    @Query("DELETE FROM favorites_table WHERE url= :favoriteUrl")
    void deleteFavorite(String favoriteUrl);

    @Query("DELETE FROM favorites_table")
    void deleteAll();

    @Query("SELECT * FROM favorites_table ORDER BY id DESC")
    LiveData<List<Favorite>> getFavoritesUpdate();

}
