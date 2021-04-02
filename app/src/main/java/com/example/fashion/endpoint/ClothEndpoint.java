package com.example.fashion.endpoint;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ClothEndpoint {
    @GET("/cloth/page/{page}")
    Call<Map<String, ArrayList<String>>> getPage(@Path("page") int page);

    @GET("/cloth/{id}")
    Call<ResponseBody> getCloth(@Path("id") String id);

    @POST("/cloth/try")
    Call<ResponseBody> tryCloth(@Body RequestBody params);
}
