package barisic.newsgetter.helper_classes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import barisic.newsgetter.db_classes.Source;
import barisic.newsgetter.db_classes.SourceDAO;
import barisic.newsgetter.db_classes.SourceDatabase;

public class SourcesRepository {
    private SourceDAO sourceDAO;
    private LiveData<List<Source>> sourcesUpdate;
    private List<Source> allSources;

    private static boolean ifSourceExists = false;

    public SourcesRepository(Application application){
        SourceDatabase database = SourceDatabase.getInstance(application);
        sourceDAO = database.sourceDAO();
        sourcesUpdate = sourceDAO.getSourcesUpdate();
        allSources = sourceDAO.getAllSources();
    }

    public void insertSource(Source source){
        if(!checkSources(source.getDomain())){
            new InsertSourceAsync(sourceDAO).execute(source);
        }
    }
    public void updateSource(Source source){
        new UpdateSourceAsync(sourceDAO).execute(source);
    }
    public void deleteSource(Source source){
        new DeleteSourceAsync(sourceDAO).execute(source);
    }
    public void deleteAll(){

    }
    public boolean checkSources(String domain){
        new CheckSourceAsync(sourceDAO).execute(domain);
        return ifSourceExists;
    }
    public LiveData<List<Source>> getSourcesUpdate(){
        return sourcesUpdate;
    }
    public List<Source> getAllSources(){
        return allSources;
    }

    private static class InsertSourceAsync extends AsyncTask<Source, Void, Void>{

        private SourceDAO sourceDAO;

        public InsertSourceAsync(SourceDAO sourceDAO){
            this.sourceDAO = sourceDAO;
        }


        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.insertSource(sources[0]);
            return null;
        }
    }
    private static class UpdateSourceAsync extends AsyncTask<Source, Void, Void>{

        private SourceDAO sourceDAO;

        public UpdateSourceAsync(SourceDAO sourceDAO){
            this.sourceDAO = sourceDAO;
        }


        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.updateSource(sources[0]);
            return null;
        }
    }
    private static class DeleteSourceAsync extends AsyncTask<Source, Void, Void>{

        private SourceDAO sourceDAO;

        public DeleteSourceAsync(SourceDAO sourceDAO){
            this.sourceDAO = sourceDAO;
        }


        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.deleteSource(sources[0]);
            return null;
        }
    }
    private static class CheckSourceAsync extends AsyncTask<String, Void, Void>{

        private SourceDAO sourceDAO;

        public CheckSourceAsync(SourceDAO sourceDao){
            this.sourceDAO = sourceDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            ifSourceExists = sourceDAO.checkSources(strings[0]);
            return null;
        }
    }
}
