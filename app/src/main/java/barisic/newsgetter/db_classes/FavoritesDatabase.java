package barisic.newsgetter.db_classes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import barisic.newsgetter.interfaces.FavoriteDAO;

@Database(version = 1, entities = Favorite.class)
public abstract class FavoritesDatabase extends RoomDatabase {

    private static FavoritesDatabase instance;
    public abstract FavoriteDAO favoriteDAO();

    public static synchronized FavoritesDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), FavoritesDatabase.class, "favorites_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }
}
