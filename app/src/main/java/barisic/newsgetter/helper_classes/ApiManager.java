package barisic.newsgetter.helper_classes;

import barisic.newsgetter.interfaces.NewsApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton klasa koja upravlja api pozivima.
 */
public class ApiManager {
    static ApiManager instance;
    private NewsApiService service;
    private ApiManager(){
        Retrofit.Builder builder = new Retrofit.Builder();
        //postavljanje retrofit-a
        Retrofit retrofit = builder.baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(NewsApiService.class);
    }
    public static ApiManager getInstance(){
        if (instance == null){
            instance = new ApiManager();
        }
        return instance;
    }
    public NewsApiService service(){
        return service;
    }
}
