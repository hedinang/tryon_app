package com.example.fashion.endpoint;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;
import java.util.Map;

public interface ClothEndpoint {
    @GET("/cloth/page/{page}")
    Observable<Map<String, List<String>>> getPage(@Path("page") int page);

    @GET("/cloths/{id}")
    Observable<ResponseBody> getCloth(@Path("id") String id);
}
