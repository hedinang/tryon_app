package com.example.fashion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fashion.adapter.ClothsAdapter;
import com.example.fashion.endpoint.ClothEndpoint;
import com.example.fashion.service.ClothService;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Context context;
    Button button;
    private static int RESULT_LOAD_IMAGE = 1;
    Bitmap person;
    String clothPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        button = findViewById(R.id.tryFashion);
        pickCloths();
        pickPerson();
        showCloths();
        tryFashion();
    }

    private void pickPerson() {
        ImageView imageView = findViewById(R.id.person);
        imageView.setOnClickListener(v -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = findViewById(R.id.person);
            person = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), 192, 256, false);
            imageView.setImageBitmap(person);

        }
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
        ImageView result = findViewById(R.id.result);
        result.setImageDrawable(null);
        Intent intent = getIntent();
        Bitmap cloth = intent.getParcelableExtra("cloth");
        clothPath = intent.getStringExtra("path");
        if (cloth != null) {
            ImageView imageView = findViewById(R.id.cloth);
            imageView.setImageBitmap(cloth);
        }
    }

    public void tryFashion() {
        button.setOnClickListener(v -> {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://acd233218e58.ngrok.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                ClothEndpoint clothEndpoint = retrofit.create(ClothEndpoint.class);
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                FileOutputStream fos = new FileOutputStream(file);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                person.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                fos.write(stream.toByteArray());
                fos.close();
                MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("person", "person.jpg",
                                RequestBody.create(MediaType.parse("image/*"), file))
                        .addFormDataPart("cloth", clothPath)
                        .build();
                clothEndpoint.tryCloth(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            byte[] result = response.body().bytes();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                            ImageView imageView = findViewById(R.id.result);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


//            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction("FILE_DOWNLOADED_ACTION");
//            registerReceiver(intentReceiver, intentFilter);
//            Intent intentSend = new Intent(context, ShowService.class);
//            intentSend.putExtra("cloth", clothPath);
//            intentSend.putExtra("person", person);
//            startService(intentSend);
        });
    }

//    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            byte[] cloth = intent.getByteArrayExtra("result");
//            Bitmap bitmap = BitmapFactory.decodeByteArray(cloth, 0, cloth.length);
//            ImageView imageView = findViewById(R.id.result);
//            imageView.setImageBitmap(bitmap);
//            Intent stopIntent = new Intent(context, ShowService.class);
//            stopService(stopIntent);
//        }
//    };
}