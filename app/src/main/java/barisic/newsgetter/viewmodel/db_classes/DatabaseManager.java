package barisic.newsgetter.viewmodel.db_classes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import barisic.newsgetter.model.db_classes.Favorite;
import barisic.newsgetter.model.db_classes.Source;
import barisic.newsgetter.viewmodel.interfaces.FavoriteDAO;
import barisic.newsgetter.viewmodel.interfaces.SourceDAO;

@Database(version = 1, entities = {Source.class, Favorite.class})
public abstract class DatabaseManager extends RoomDatabase {

    private static DatabaseManager sourceInstance;
    private static DatabaseManager favoriteInstance;

    public abstract SourceDAO sourceDAO();
    public abstract FavoriteDAO favoriteDAO();

    //synchronized limits access to only one thread at given time
    public static synchronized DatabaseManager getSourcesInstance(Context context){
        if(sourceInstance == null){
            //fallbackToDestructiveMigration() on version change delete db and make a new one
            sourceInstance = Room.databaseBuilder(context.getApplicationContext(), DatabaseManager.class, "source_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return sourceInstance;
    }

    public static synchronized DatabaseManager getFavoritesInstance(Context context) {
        if(favoriteInstance == null){
            favoriteInstance = Room.databaseBuilder(context.getApplicationContext(), DatabaseManager.class, "favorites_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return favoriteInstance;
    }
}
