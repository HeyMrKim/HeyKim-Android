package com.example.neeraj.ticketsnew;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private static initMyApi initMyApi;
    private static initMyApi2 initMyApi2;
    //사용하고 있는 서버 BASE 주소
    private static String baseUrl = "http://3.38.115.233:8080/";



    private RetrofitClient() {
        //로그를 보기 위한 Interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) //로그 기능 추가
                .build();

        initMyApi = retrofit.create(initMyApi.class);
        initMyApi2 = retrofit.create(initMyApi2.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public static initMyApi getRetrofitInterface() {
        return initMyApi;
    }
    public static initMyApi2 getRetrofitInterface2() { return initMyApi2; }
}
