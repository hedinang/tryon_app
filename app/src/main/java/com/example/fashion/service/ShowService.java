//package com.example.fashion.service;
//
//import android.app.IntentService;
//import android.content.Intent;
//import com.example.fashion.endpoint.ClothEndpoint;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class ShowService extends IntentService {
//
//
//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     *
//     * @param name Used to name the worker thread, important only for debugging.
//     */
//    ClothEndpoint clothEndpoint;
//
//    public ShowService() {
//        super("show service");
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.131:5000/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//        clothEndpoint = retrofit.create(ClothEndpoint.class);
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        try {
//            String cloth = intent.getStringExtra("cloth");
//            byte[] person = intent.getByteArrayExtra("person");
//            File file = new File("person.jpg");
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(person);
//            fos.close();
//            RequestBody personPart = RequestBody.create(MultipartBody.FORM, file);
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction("FILE_DOWNLOADED_ACTION");
//            broadcastIntent.putExtra("result", clothEndpoint.tryCloth(personPart, cloth).execute().body().bytes());
//            sendBroadcast(broadcastIntent);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
