package com.vst.rxjava20;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by user on 2017/3/31.
 */

public interface ApiServer {
    String HOST = "http://192.168.3.155/index.php/";

    @GET("account/getinfo")
    Observable<ResponseBody> getInfo2();

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("account/post333")
    Observable<ResponseBody> post8(@Body RequestBody params);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("account/post3")
    Observable<ResponseBody> post9(@Body RequestBody params);
}
