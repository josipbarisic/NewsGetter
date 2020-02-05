package barisic.newsgetter.helper_classes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import barisic.newsgetter.db_classes.DatabaseManager;
import barisic.newsgetter.db_classes.Favorite;
import barisic.newsgetter.interfaces.FavoriteDAO;

public class FavoriteViewModel extends AndroidViewModel {
    private FavoriteDAO favoriteDAO;
    private LiveData<List<Favorite>> favoritesLiveList;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favoriteDAO = DatabaseManager.getFavoritesInstance(application).favoriteDAO();
        favoritesLiveList = favoriteDAO.getFavoritesUpdate();
    }

    public void insertFavorite(Favorite favorite){
        new InsertFavoriteAsync(favoriteDAO).execute(favorite);
    }
    public void deleteFavorite(String url){
        new DeleteFavoriteAsync(favoriteDAO).execute(url);
    }
    public void deleteAllFavorites(){
        favoriteDAO.deleteAll();
    }
    public LiveData<List<Favorite>> getAllFavorites(){
        return favoritesLiveList;
    }

    private class InsertFavoriteAsync extends AsyncTask<Favorite, Void, Void>{

        private FavoriteDAO favoriteDAO;

        public InsertFavoriteAsync(FavoriteDAO favoriteDAO){
            this.favoriteDAO = favoriteDAO;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDAO.insertFavorite(favorites[0]);
            return null;
        }
    }

    private class DeleteFavoriteAsync extends AsyncTask<String, Void, Void>{

        private FavoriteDAO favoriteDAO;

        public DeleteFavoriteAsync(FavoriteDAO favoriteDAO){
            this.favoriteDAO = favoriteDAO;
        }

        @Override
        protected Void doInBackground(String... favorites) {
            favoriteDAO.deleteFavorite(favorites[0]);
            return null;
        }
    }
}
