package com.example.android.currencyconversion.network;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.android.currencyconversion.models.ConvertResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertRepository {

    private List<ConvertResponse> listOfRates;
    private final MutableLiveData<List<ConvertResponse>> listOfRatesLiveData = new MutableLiveData<>();

    public ConvertRepository(Application application) {
    }

    public void getListOfRates() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        //Execute the Network request
        Call<List<ConvertResponse>> call = apiService.getCurrencyRates();

        //Execute the request in a background thread
        call.enqueue(new Callback<List<ConvertResponse>>() {
            @Override
            public void onResponse(Call<List<ConvertResponse>> call, Response<List<ConvertResponse>> response) {
                listOfRates = response.body();
            }

            @Override
            public void onFailure(Call<List<ConvertResponse>> call, Throwable t) {
                //Smth went wrong
            }
        });
    }

    public MutableLiveData<List<ConvertResponse>> getListOfRatesLiveData() {
        getListOfRates();
        listOfRatesLiveData.setValue(listOfRates);
        return listOfRatesLiveData;
    }
}
