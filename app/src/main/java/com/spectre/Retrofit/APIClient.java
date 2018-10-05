package com.spectre.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        String BASE_URL1="http://rootapp.nirvanainfoway.com/restapi/";
        String BASE_URL="https://spectrecar.com/api/v1/spectre_api.php/";
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.MINUTES).readTimeout(10, TimeUnit.MINUTES).writeTimeout(10, TimeUnit.MINUTES).addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }
}

