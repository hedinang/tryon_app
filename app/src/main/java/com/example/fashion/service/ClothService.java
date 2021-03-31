package com.example.fashion.service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fashion.MainActivity;
import com.example.fashion.adapter.ClothsAdapter;
import com.example.fashion.endpoint.ClothEndpoint;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClothService {
    ClothEndpoint clothEndpoint;
    public final MutableLiveData<Boolean> addSuccess = new MutableLiveData<>();

    protected <T> Observable<T> preConsumer(Observable<T> observable) {
        return observable.compose(upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io()));
    }

    public ClothService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.131:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        clothEndpoint = retrofit.create(ClothEndpoint.class);
    }

    public void getPage(int page, ClothsAdapter clothsAdapter) {
        clothEndpoint.getPage(page).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, List<String>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("aaaa");
                    }

                    @Override
                    public void onNext(@NonNull Map<String, List<String>> stringListMap) {
                        clothsAdapter.setData(stringListMap.get("cloths"));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("aaaa");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("aaaa");
                    }
                });


    }

    public void showCloth(String id, ImageView imageView) {
        clothEndpoint.getCloth(id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("aaaa");
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody response) {
                        byte[] bytes = new byte[0];
                        try {
                            bytes = response.bytes();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(bitmap);
                        imageView.setOnClickListener(v -> {
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            intent.putExtra("cloth", bitmap);
                            v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("aaaa");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("aaaa");
                    }
                });

    }
}
