package com.example.android.currencyconversion.models;

public class ConvertResponseWrapper {

    public double result;
    private Throwable error;

    public ConvertResponseWrapper(double result) {
        this.result = result;
        this.error = null;
    }

    public ConvertResponseWrapper(Throwable error) {
        this.result = 0.0;
        this.error = error;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
