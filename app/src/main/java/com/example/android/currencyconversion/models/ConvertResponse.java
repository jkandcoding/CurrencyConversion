package com.example.android.currencyconversion.models;

import com.google.gson.annotations.SerializedName;

public class ConvertResponse {

    @SerializedName("currency_code")
    private String currency_code;

    @SerializedName("unit_value")
    private double unit_value;

    @SerializedName("buying_rate")
    private double buying_rate;

    @SerializedName("median_rate")
    private double median_rate;

    @SerializedName("selling_rate")
    private double selling_rate;

    public ConvertResponse(String currency_code, double unit_value, double buying_rate, double median_rate, double selling_rate) {
        this.currency_code = currency_code;
        this.unit_value = unit_value;
        this.buying_rate = buying_rate;
        this.median_rate = median_rate;
        this.selling_rate = selling_rate;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public double getUnit_value() {
        return unit_value;
    }

    public void setUnit_value(double unit_value) {
        this.unit_value = unit_value;
    }

    public double getBuying_rate() {
        return buying_rate;
    }

    public void setBuying_rate(double buying_rate) {
        this.buying_rate = buying_rate;
    }

    public double getMedian_rate() {
        return median_rate;
    }

    public void setMedian_rate(double median_rate) {
        this.median_rate = median_rate;
    }

    public double getSelling_rate() {
        return selling_rate;
    }

    public void setSelling_rate(double selling_rate) {
        this.selling_rate = selling_rate;
    }

    @Override
    public String toString() {
        return "ConvertResponse{" +
                "currency_code='" + currency_code + '\'' +
                ", unit_value=" + unit_value +
                ", buying_rate=" + buying_rate +
                ", median_rate=" + median_rate +
                ", selling_rate=" + selling_rate +
                '}';
    }
}
