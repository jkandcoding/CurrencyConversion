package com.example.android.currencyconversion.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.currencyconversion.models.ConvertResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertRepository {

    private List<ConvertResponse> listOfRates;
    private MutableLiveData<List<ConvertResponse>> listOfRatesLiveData = new MutableLiveData<>();

    public void getListOfRates(MyCallback callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        //Execute the Network request
        Call<List<ConvertResponse>> call = apiService.getCurrencyRates();

        //Execute the request in a background thread
        call.enqueue(new Callback<List<ConvertResponse>>() {
            @Override
            public void onResponse(Call<List<ConvertResponse>> call, Response<List<ConvertResponse>> response) {
                if (response.body() != null) {
                    listOfRates = response.body();
                    callback.onDataGot(listOfRates);
                }
            }

            @Override
            public void onFailure(Call<List<ConvertResponse>> call, Throwable t) {
                //Smth went wrong
            }
        });
    }

    public LiveData<List<ConvertResponse>> getListOfRatesLiveData() {
        getListOfRates(response -> listOfRatesLiveData.postValue(response));

        return listOfRatesLiveData;
    }

    public interface MyCallback {
        void onDataGot(List<ConvertResponse> response);
    }
}
