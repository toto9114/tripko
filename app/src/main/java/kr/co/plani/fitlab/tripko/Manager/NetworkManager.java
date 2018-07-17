package kr.co.plani.fitlab.tripko.Manager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by toto9114 on 2017-01-20.
 */

public class NetworkManager {
    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    //    private static final String BASE_URL = "http://192.168.50.100:8080/";
    private static final String BASE_URL = "http://api-dev.tripko.ga:7021/";
//    private static final String BASE_URL = "http://api-dev.tripko.ga:7011/";


    private Retrofit retrofit;
    private NetworkService service;

    private NetworkManager() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(NetworkService.class);
    }

    public NetworkService getService() {
        return service;
    }
}
