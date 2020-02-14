package barisic.newsgetter.viewmodel.db_classes;
import android.app.Application;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import barisic.newsgetter.model.db_classes.Source;
import barisic.newsgetter.viewmodel.interfaces.SourceDAO;

public class SourceViewModel extends AndroidViewModel {
    private SourceDAO sourceDAO;
    private LiveData<List<Source>> sourcesUpdate;
    private List<Source> allSources;

    public SourceViewModel(@NonNull Application application) {
        super(application);
        sourceDAO = DatabaseManager.getSourcesInstance(application).sourceDAO();
        sourcesUpdate = sourceDAO.getSourcesUpdate();
        allSources = sourceDAO.getAllSources();
    }

    public void insertSource(Source source){
        new InsertSourceAsync(sourceDAO).execute(source);
    }
    public void deleteAllSources(){
        sourceDAO.deleteAll();
    }
    public LiveData<List<Source>> getSourcesUpdate(){
        return sourcesUpdate;
    }
    public List<Source> getAllSources(){
        return allSources;
    }

    //async classes
    private static class InsertSourceAsync extends AsyncTask<Source, Void, Void> {

        private SourceDAO sourceDAO;

        private InsertSourceAsync(SourceDAO sourceDAO){
            this.sourceDAO = sourceDAO;
        }


        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.insertSource(sources[0]);
            return null;
        }
    }
}