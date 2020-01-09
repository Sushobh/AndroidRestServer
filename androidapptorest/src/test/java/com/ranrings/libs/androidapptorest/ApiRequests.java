package com.ranrings.libs.androidapptorest;

import com.google.gson.GsonBuilder;

import com.google.gson.JsonElement;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;


import java.util.HashMap;
import java.util.Map;

public class ApiRequests {

    private static Retrofit retrofit;


    public static Retrofit getVeryOwnRetrofit(int port) {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .client(getHttpClientBuilder().build())
                    .baseUrl("http://localhost:"+port)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    private static OkHttpClient.Builder getHttpClientBuilder() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        return httpClient;
    }



    public interface POSTService  {
        @POST
        Observable<JsonElement> makeRequest(@Url String url, @Body Map<String, Object> loginBody);
    }

    public interface GETService  {
        @POST
        Observable<JsonElement> makeRequest(@Url String url);
    }

}