package com.vst.rxjava20;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 2017/3/31.
 */

public class VRetrofit {
    private static volatile VRetrofit mInstance;
    private final Retrofit mRetrofit;

    private VRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.connectTimeout(20, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            //Okhttp拦截器:打印log
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        mRetrofit = new Retrofit.Builder().baseUrl(ApiServer.HOST)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static VRetrofit getInstance() {
        if (mInstance == null) {
            synchronized (VRetrofit.class) {
                if (mInstance == null) {
                    mInstance = new VRetrofit();
                }
            }
        }
        return mInstance;
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}
