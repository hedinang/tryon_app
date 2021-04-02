package com.example.fashion.service;

import android.app.IntentService;
import android.content.Intent;
import com.example.fashion.endpoint.ClothEndpoint;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;

public class ClothService extends IntentService {
    ClothEndpoint clothEndpoint;

    public ClothService() {
        super("cloth service");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://acd233218e58.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        clothEndpoint = retrofit.create(ClothEndpoint.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ArrayList<String> paths = clothEndpoint.getPage(1).execute().body().get("cloths");
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("FILE_DOWNLOADED_ACTION");
            broadcastIntent.putExtra("paths", paths);
            for (String path : paths)
                broadcastIntent.putExtra(path, clothEndpoint.getCloth(path).execute().body().bytes());
            sendBroadcast(broadcastIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}