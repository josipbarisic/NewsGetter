package barisic.newsgetter.helper_classes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import barisic.newsgetter.db_classes.Source;

public class SourceViewModel extends AndroidViewModel {
    private SourcesRepository repository;
    private LiveData<List<Source>> sourcesUpdate;
    private List<Source> allSources;

    public SourceViewModel(@NonNull Application application) {
        super(application);
        repository = new SourcesRepository(application);
        sourcesUpdate = repository.getSourcesUpdate();
        allSources = repository.getAllSources();
    }

    public void insertSource(Source source){
        repository.insertSource(source);
    }
    public void updateSource(Source source){
        repository.updateSource(source);
    }
    public void deleteSource(Source source){
        repository.deleteSource(source);
    }
    public boolean checkSources(String domain){
        return repository.checkSources(domain);
    }
    public LiveData<List<Source>> getSourcesUpdate(){
        return sourcesUpdate;
    }
    public List<Source> getAllSources(){
        return allSources;
    }
}
