package com.example.android.currencyconversion.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.android.currencyconversion.models.ConvertInputs;
import com.example.android.currencyconversion.models.ConvertResponseWrapper;
import com.example.android.currencyconversion.network.ConvertRepository;

public class ConvertViewModel extends AndroidViewModel {

    private final ConvertRepository convertRepository;
    private final MediatorLiveData<ConvertResponseWrapper> wraper;
    ConvertInputs viewModelInputs;
    String resultForUI = "";

    public ConvertViewModel(@NonNull Application application) {
        super(application);
        convertRepository = new ConvertRepository();
        wraper = new MediatorLiveData<>();
        viewModelInputs = null;
    }

    public LiveData<ConvertResponseWrapper> getResult() {
        convertRepository.setInputs(viewModelInputs);
        wraper.addSource(convertRepository.getListOfRates(), wraper::setValue);
        return wraper;
    }

    public String getResultForUI() {
        return resultForUI;
    }

    public void setResultForUI(String resultForUI) {
        this.resultForUI = resultForUI;
    }

    public void setViewModelInputs(ConvertInputs viewModelInputs) {
        this.viewModelInputs = viewModelInputs;
    }

    public ConvertInputs getViewModelInputs() {
        return viewModelInputs;
    }
}
