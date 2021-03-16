package com.example.android.currencyconversion.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.android.currencyconversion.models.ConvertResponse;
import com.example.android.currencyconversion.models.ConvertResponseWrapper;
import com.example.android.currencyconversion.network.ConvertRepository;

import java.util.List;

public class ConvertViewModel extends AndroidViewModel {

    private final ConvertRepository convertRepository;
    private MediatorLiveData<ConvertResponseWrapper> wraper;
    //private LiveData<List<ConvertResponse>> currencyRates;

    public ConvertViewModel(@NonNull Application application) {
        super(application);
        convertRepository = new ConvertRepository();
        wraper = new MediatorLiveData<>();
    }

    public LiveData<ConvertResponseWrapper> getRates() {
        Log.d("dfggfdg", "viewModel");
        wraper.addSource(convertRepository.getListOfRates(), new Observer<ConvertResponseWrapper>() {
            @Override
            public void onChanged(ConvertResponseWrapper convertResponseWrapper) {
                wraper.setValue(convertResponseWrapper);
            }
        });
        return wraper;
    }


//    public LiveData<List<ConvertResponse>> getCurrencyRates() {
//        if (currencyRates == null) {
//            currencyRates = new MutableLiveData<>();
//            loadCurrencyRates();
//        }
//        return currencyRates;
//    }
//
//    private void loadCurrencyRates() {
//        currencyRates = convertRepository.getListOfRatesLiveData();
//    }
}
