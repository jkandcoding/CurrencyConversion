package com.example.android.currencyconversion.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.android.currencyconversion.models.ConvertResponse;
import com.example.android.currencyconversion.network.ConvertRepository;

import java.util.List;

public class ConvertViewModel extends AndroidViewModel {

    private final ConvertRepository convertRepository;
    private MutableLiveData<List<ConvertResponse>> currencyRates;

    public ConvertViewModel(@NonNull Application application) {
        super(application);
        convertRepository = new ConvertRepository(application);
    }

    public MutableLiveData<List<ConvertResponse>> getCurrencyRates() {
        if (currencyRates == null) {
            currencyRates = new MutableLiveData<>();
            loadCurrencyRates();
        }
        return currencyRates;
    }

    private void loadCurrencyRates() {
        currencyRates = convertRepository.getListOfRatesLiveData();
    }
}
