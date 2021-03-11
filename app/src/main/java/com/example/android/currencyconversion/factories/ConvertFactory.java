package com.example.android.currencyconversion.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.currencyconversion.viewModels.ConvertViewModel;

public class ConvertFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Application application;

    public ConvertFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ConvertViewModel.class) {
            return (T) new ConvertViewModel(application);
        }
        return null;
    }
}
