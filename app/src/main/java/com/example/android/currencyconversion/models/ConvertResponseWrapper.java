package com.example.android.currencyconversion.models;

import java.util.List;

public class ConvertResponseWrapper {
    public List<ConvertResponse> rates;
    private Throwable error;

    public ConvertResponseWrapper(List<ConvertResponse> rates) {
        this.rates = rates;
        this.error = null;
    }

    public ConvertResponseWrapper(Throwable error) {
        this.rates = null;
        this.error = error;
    }

    public List<ConvertResponse> getRates() {
        return rates;
    }

    public void setRates(List<ConvertResponse> rates) {
        this.rates = rates;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
