package com.example.fashion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fashion.adapter.ClothsAdapter;
import com.example.fashion.service.ClothService;

import java.util.ArrayList;

public class ClothActivity extends AppCompatActivity {
    public static final String mBroadcastAction = "STRING_BROADCAST_ACTION";
    IntentFilter intentFilter;
    RecyclerView recyclerView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth);
        intentFilter = new IntentFilter();
        intentFilter.addAction("FILE_DOWNLOADED_ACTION");
        context = getApplicationContext();

        registerReceiver(intentReceiver, intentFilter);
        Intent intent = new Intent(this, ClothService.class);
        startService(intent);

    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> paths = intent.getStringArrayListExtra("paths");
            ArrayList<Bitmap> listBitmaps = new ArrayList<>();
            for (String path : paths) {
                byte[] cloth = intent.getByteArrayExtra(path);
                Bitmap bitmap = BitmapFactory.decodeByteArray(cloth, 0, cloth.length);
                listBitmaps.add(bitmap);
            }
            ClothsAdapter clothsAdapter = new ClothsAdapter(context, listBitmaps, paths);
            recyclerView = findViewById(R.id.recycle);
            recyclerView.setAdapter(clothsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(ClothActivity.this));
            System.out.println("aaaa");
            Intent stopIntent = new Intent(ClothActivity.this, ClothService.class);
            stopService(stopIntent);
        }
    };

}