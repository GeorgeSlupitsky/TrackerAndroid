package com.micro_gis.microgistracker.modules;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.micro_gis.microgistracker.retrofit.API;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User9 on 12.03.2018.
 */

@Module
public class APIModule {

    @Provides
    public API api(Retrofit retrofit){
        return retrofit.create(API.class);
    }

    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient, URL url, GsonConverterFactory gsonConverterFactory){
        String urlStr = url.toString();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(urlStr)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public URL url(@Named("urlServer") String urlServer) {
        if (urlServer == null){
            urlServer = "null";
        }

        if (!urlServer.contains("http://")){
            urlServer = "http://" + urlServer;
        }

        URL url = null;
        try {
            url = new URL(urlServer);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Provides
    public OkHttpClient okHttpClient(){
        return new OkHttpClient()
                .newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Named("urlServer")
    public String provideUrlServer(SharedPreferences sharedPreferences){
        return sharedPreferences.getString("url", "");
    }

}
