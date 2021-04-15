package com.example.android.currencyconversion.network;

import com.example.android.currencyconversion.MyApplication;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://hnbex.eu/api/v1/";
    static final String HEADER_PRAGMA = "Pragma";
    static final String HEADER_CACHE_CONTROL = "Cache-Control";


    /**
     * This method returns retrofit client instance
     *
     * @return Retrofit object
     */
    public static Retrofit getClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        File httpCacheDirectory = new File(MyApplication.getInstance().getCacheDir(), "offlineCache");
        Cache myCache = new Cache(httpCacheDirectory, 5 * 1024 * 1024);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(myCache)
                .addInterceptor(logging)
                .addNetworkInterceptor(new CacheNetworkInterceptor())   // only used when network is on
                .addInterceptor(new ForceCacheAppInterceptor())
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
