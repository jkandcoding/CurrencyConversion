package com.example.android.currencyconversion.network;

import com.example.android.currencyconversion.models.ConvertResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("rates/daily")
    Call<List<ConvertResponse>> getCurrencyRates();
}
