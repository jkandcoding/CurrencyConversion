package com.example.android.currencyconversion.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.currencyconversion.models.ConvertInputs;
import com.example.android.currencyconversion.models.ConvertResponse;
import com.example.android.currencyconversion.models.ConvertResponseWrapper;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertRepository {


    public MutableLiveData<ConvertInputs> input = new MutableLiveData<>();

    public LiveData<ConvertResponseWrapper> getListOfRates() {
        final MutableLiveData<ConvertResponseWrapper> wrapper = new MutableLiveData<>();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        //Execute the Network request
        Call<List<ConvertResponse>> call = apiService.getCurrencyRates();

        //Execute the request in a background thread
        call.enqueue(new Callback<List<ConvertResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ConvertResponse>> call, @NonNull Response<List<ConvertResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // does the response come from network or cache:
                    if (response.raw().networkResponse() != null) {
                        Log.d("http", "onResponse: response is from NETWORK");
                    } else if (response.raw().cacheResponse() != null) {
                        Log.d("http", "onResponse: response is from CACHE");
                    }

                    List<ConvertResponse> listOfRates = response.body();
                    if (input.getValue() != null) {
                        wrapper.postValue(new ConvertResponseWrapper(doConversion(listOfRates)));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ConvertResponse>> call, @NonNull Throwable t) {
                //Smth went wrong
                wrapper.postValue(new ConvertResponseWrapper(t));
            }
        });
        return wrapper;
    }

    private double doConversion(List<ConvertResponse> currencyRates) {
        double amount = Objects.requireNonNull(input.getValue()).getAmount();
        boolean isBuyingRate = input.getValue().isBuyingRate();
        double result;
        ConvertResponse fromObject = null;
        ConvertResponse toObject = null;

        // choosing ConvertResponse object based on spinner's output
        for (ConvertResponse response : currencyRates) {
            if (response.getCurrency_code().equals(input.getValue().getFromCurrency())) {
                fromObject = response;
            }
            if (response.getCurrency_code().equals(input.getValue().getToCurrency())) {
                toObject = response;
            }
        }

        // if "HRK" is neither fromCurrency or toCurrency
        if (!input.getValue().getFromCurrency().equals("HRK") && !input.getValue().getToCurrency().equals("HRK") && fromObject != null && toObject != null) {

            if (isBuyingRate) {
                result = amount * fromObject.getSelling_rate() / toObject.getBuying_rate();
            } else {
                result = amount * fromObject.getBuying_rate() / toObject.getSelling_rate();
            }

            // if only fromCurrency is "HRK"
        } else if (input.getValue().getFromCurrency().equals("HRK") && !input.getValue().getToCurrency().equals("HRK") && toObject != null) {

            if (isBuyingRate) {
                result = amount / toObject.getBuying_rate();
            } else {
                result = amount / toObject.getSelling_rate();
            }

            // if only toCurrency is "HRK"
        } else if (!input.getValue().getFromCurrency().equals("HRK") && input.getValue().getToCurrency().equals("HRK") && fromObject != null) {

            if (isBuyingRate) {
                result = amount * fromObject.getSelling_rate();
            } else {
                result = amount * fromObject.getBuying_rate();
            }

            // if "HRK" is both fromCurrency and toCurrency
        } else {
            result = 0.0;
        }

        // if selected currencies are "HUF" or "JPY" result needs correction
        if (input.getValue().getFromCurrency().equals("HUF") || input.getValue().getFromCurrency().equals("JPY")) {
            result = result / 100;
        }

        if (input.getValue().getToCurrency().equals("HUF") || input.getValue().getToCurrency().equals("JPY")) {
            result = result * 100;
        }

        return result;
    }

    public void setInputs(ConvertInputs inputs) {
        input.postValue(inputs);
    }


}
