package com.example.android.currencyconversion.network;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.currencyconversion.models.ConvertInputs;
import com.example.android.currencyconversion.models.ConvertResponse;
import com.example.android.currencyconversion.models.ConvertResponseWrapper;

import java.util.List;

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
                if (response.isSuccessful()) {
                    List<ConvertResponse> listOfRates = response.body();
                    wrapper.postValue(new ConvertResponseWrapper(doConversion(listOfRates)));
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
        double amount = input.getValue().getAmount();
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
        if (!input.getValue().getFromCurrency().equals("HRK") && !input.getValue().getToCurrency().equals("HRK")) {

            if (isBuyingRate) {
                result = amount * fromObject.getSelling_rate() / toObject.getBuying_rate();
            } else {
                result = amount * fromObject.getBuying_rate() / toObject.getSelling_rate();
            }

            // if only fromCurrency is "HRK"
        } else if (input.getValue().getFromCurrency().equals("HRK") && !input.getValue().getToCurrency().equals("HRK")) {

            if (isBuyingRate) {
                result = amount / toObject.getSelling_rate();
            } else {
                result = amount / toObject.getBuying_rate();
            }

            // if only toCurrency is "HRK"
        } else if (!input.getValue().getFromCurrency().equals("HRK") && input.getValue().getToCurrency().equals("HRK")) {

            if (isBuyingRate) {
                result = amount * fromObject.getSelling_rate();
            } else {
                result = amount * fromObject.getBuying_rate();
            }

            // if "HRK" is both fromCurrency and toCurrency
        } else {
            result = 0.0;
        }
        return result;
    }

    public void setInputs(ConvertInputs inputs) {
        input.postValue(inputs);
    }

}
