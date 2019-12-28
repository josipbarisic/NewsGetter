package barisic.newsgetter.db_classes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = Source.class)
public abstract class SourceDatabase extends RoomDatabase {

    private static SourceDatabase instance;
    public abstract SourceDAO sourceDAO();

    //synchronized limits access to only one thread at given time
    public static synchronized SourceDatabase getInstance(Context context){
        if(instance == null){
            //fallbackToDestructiveMigration() on version change delete db and make a new one
            instance = Room.databaseBuilder(context.getApplicationContext(), SourceDatabase.class, "source_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }
}
