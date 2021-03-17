package com.example.android.currencyconversion.models;

public class ConvertInputs {

    private double amount;
    private String fromCurrency;
    private String toCurrency;
    private boolean isBuyingRate;

    public ConvertInputs(double amount, String fromCurrency, String toCurrency, boolean isBuyingRate) {
        this.amount = amount;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.isBuyingRate = isBuyingRate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public boolean isBuyingRate() {
        return isBuyingRate;
    }

    public void setBuyingRate(boolean buyingRate) {
        isBuyingRate = buyingRate;
    }
}
