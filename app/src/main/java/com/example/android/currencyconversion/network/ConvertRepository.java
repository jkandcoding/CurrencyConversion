package com.example.android.currencyconversion.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.currencyconversion.models.ConvertResponse;
import com.example.android.currencyconversion.models.ConvertResponseWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertRepository {

    private List<ConvertResponse> listOfRates;
    private MutableLiveData<List<ConvertResponse>> listOfRatesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> error = new MutableLiveData<>();

    public LiveData<ConvertResponseWrapper> getListOfRates() {
        final MutableLiveData<ConvertResponseWrapper> wrapper = new MutableLiveData<>();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        //Execute the Network request
        Call<List<ConvertResponse>> call = apiService.getCurrencyRates();

        //Execute the request in a background thread
        call.enqueue(new Callback<List<ConvertResponse>>() {
            @Override
            public void onResponse(Call<List<ConvertResponse>> call, Response<List<ConvertResponse>> response) {
                if (response.isSuccessful()) {
                   // listOfRates = response.body();
                    wrapper.postValue(new ConvertResponseWrapper(response.body()));
                  //  callback.onDataGot(listOfRates);
                }
            }

            @Override
            public void onFailure(Call<List<ConvertResponse>> call, Throwable t) {
                //Smth went wrong
                wrapper.postValue(new ConvertResponseWrapper(t));
            }
        });
        return wrapper;
    }

//    public LiveData<List<ConvertResponse>> getListOfRatesLiveData() {
//        getListOfRates(new MyCallback() {
//            @Override
//            public void onDataGot(List<ConvertResponse> response) {
//                listOfRatesLiveData.postValue(response);
//            }
//        });
//        return listOfRatesLiveData;
//    }
//
//    public interface MyCallback {
//        void onDataGot(List<ConvertResponse> response);
//    }
}
