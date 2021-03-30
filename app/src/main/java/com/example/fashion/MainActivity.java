package com.example.fashion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pickCloths();
        showCloths();
    }

    public void pickCloths() {
        ImageView imageView = findViewById(R.id.cloth);
        final Intent intent = new Intent(this, ClothActivity.class);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    public void showCloths() {
        Intent intent = getIntent();
        Bitmap cloth = intent.getParcelableExtra("cloth");
        if (cloth != null) {
            ImageView imageView = findViewById(R.id.cloth);
            imageView.setImageBitmap(cloth);
        }
    }
}