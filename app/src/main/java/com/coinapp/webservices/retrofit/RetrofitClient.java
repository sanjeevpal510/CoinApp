package com.coinapp.webservices.retrofit;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.coinapp.webservices.WebserviceConstant.BASE_URL;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build();
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("retro response ", message);
                }
            });
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
