package com.example.android.currencyconversion.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.android.currencyconversion.models.ConvertResponseWrapper;
import com.example.android.currencyconversion.network.ConvertRepository;

public class ConvertViewModel extends AndroidViewModel {

    private final ConvertRepository convertRepository;
    private final MediatorLiveData<ConvertResponseWrapper> wraper;

    public ConvertViewModel(@NonNull Application application) {
        super(application);
        convertRepository = new ConvertRepository();
        wraper = new MediatorLiveData<>();
    }

    public LiveData<ConvertResponseWrapper> getRates() {
        wraper.addSource(convertRepository.getListOfRates(), wraper::setValue);
        return wraper;
    }

}
