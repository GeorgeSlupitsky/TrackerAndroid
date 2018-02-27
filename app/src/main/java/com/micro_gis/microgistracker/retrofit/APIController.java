package com.micro_gis.microgistracker.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by postp on 24.02.2018.
 */

public class APIController {

    public static API getApi(String url) {
        if (url == null){
            url = "null";
        }

        if (!url.contains("http://")){
            url = "http://" + url;
        }

        try {
            URL url1 = new URL(url);
            url = url1.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(API.class);

    }

}
