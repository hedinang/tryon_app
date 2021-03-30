package com.example.fashion;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fashion.adapter.ClothsAdapter;
import com.example.fashion.endpoint.ClothEndpoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ClothActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth);
        final Intent intent = new Intent(this, MainActivity.class);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.131:5000")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        final ClothEndpoint clothEndpoint = retrofit.create(ClothEndpoint.class);
        Call<Map> call = clothEndpoint.getPage(1);
        final Context context = this.getApplicationContext();
        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                Map list = response.body();
                ArrayList<String> cloths = (ArrayList<String>) list.get("cloths");
                ClothsAdapter clothsAdapter = new ClothsAdapter(cloths,clothEndpoint);
                RecyclerView recyclerView = findViewById(R.id.recycle);
                recyclerView.setAdapter(clothsAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                System.out.println("loi connect");
            }
        });
//        call.enqueue(new Callback<Map>() {
//            @Override
//            public void onResponse(Call<Map> call, Response<Map> response) {
//                Map list = response.body();
//                ArrayList<String> cloths = (ArrayList<String>) list.get("cloths");
//                ClothsAdapter clothsAdapter = new ClothsAdapter(cloths);
//                RecyclerView recyclerView = findViewById(R.id.recycle);
//                recyclerView.setAdapter(clothsAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            }
//
//            @Override
//            public void onFailure(Call<Map> call, Throwable t) {
//                System.out.println("loi connect");
//            }
//        });
    }
}