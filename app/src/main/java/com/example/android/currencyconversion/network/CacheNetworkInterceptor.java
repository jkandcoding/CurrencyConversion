package com.example.android.currencyconversion.network;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

import static com.example.android.currencyconversion.network.ApiClient.HEADER_CACHE_CONTROL;
import static com.example.android.currencyconversion.network.ApiClient.HEADER_PRAGMA;

public class CacheNetworkInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        // Request request = chain.request();
        Response response = chain.proceed(chain.request());

        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(1, TimeUnit.HOURS)    //if network is ON, request in this next period will come from cache
                .build();

        Log.d("http", "Cache interceptor");

        return response.newBuilder()
                .removeHeader(HEADER_PRAGMA)  // potentially tells the request not to use caching ever
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build();
    }
}
