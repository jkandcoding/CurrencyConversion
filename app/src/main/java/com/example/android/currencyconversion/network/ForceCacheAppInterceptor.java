package com.example.android.currencyconversion.network;

import com.example.android.currencyconversion.MyApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.android.currencyconversion.network.ApiClient.HEADER_CACHE_CONTROL;
import static com.example.android.currencyconversion.network.ApiClient.HEADER_PRAGMA;

public class ForceCacheAppInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        // prevent caching when network is on. For that we use the CacheNetworkInterceptor
        if (!MyApplication.hasNetwork()) {
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(1, TimeUnit.DAYS)   // if offline mode -> use cache data if they are newer than this period
                    .build();

            request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build();
        }
        return chain.proceed(request);
    }
}
