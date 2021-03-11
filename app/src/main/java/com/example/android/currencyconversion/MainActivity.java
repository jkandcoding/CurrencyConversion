package com.example.android.currencyconversion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.currencyconversion.factories.ConvertFactory;
import com.example.android.currencyconversion.models.ConvertResponse;
import com.example.android.currencyconversion.viewModels.ConvertViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    List<ConvertResponse> currencyRates = new ArrayList<>();

    private TextView tv_result;
    private EditText et_amount;
    private Button btn_convert_currency;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private RadioButton rbtn_buy;

    ConvertViewModel viewModel;
    String fromCurrency;
    String toCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewModel();
        initViews();
        convertBtnPressed();

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(spinnerAdapter);
        fromSpinner.setOnItemSelectedListener(this);

        toSpinner.setAdapter(spinnerAdapter);
        toSpinner.setOnItemSelectedListener(this);
    }

    private void initViews() {
        fromSpinner = findViewById(R.id.sp_from_currency);
        toSpinner = findViewById(R.id.sp_to_currency);
        tv_result = findViewById(R.id.tv_result);
        btn_convert_currency = findViewById(R.id.btn_convert_currency);
        et_amount = findViewById(R.id.et_currency);
        rbtn_buy = findViewById(R.id.rbtn_buying_rate);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this, new ConvertFactory(getApplication())).get(ConvertViewModel.class);
        viewModel.getCurrencyRates().observe(this, rates -> {
            currencyRates = rates;
            Log.d("hghghg", "activity " + String.valueOf(currencyRates == null));
        });
    }

    private void convertBtnPressed() {
        btn_convert_currency.setOnClickListener(view -> {
            ConvertResponse fromObject = currencyRates.get(turnCurrencyToPosition(fromCurrency));
            ConvertResponse toObject = currencyRates.get(turnCurrencyToPosition(toCurrency));

            Double amount = Double.parseDouble(et_amount.getText().toString().trim());
            if (amount == null) {
                amount = 1.0;
            }

            Double result;
            if (rbtn_buy.isChecked()) {
                result = amount * fromObject.getBuying_rate() / toObject.getBuying_rate();
            } else {
                result = amount * fromObject.getSelling_rate() / toObject.getSelling_rate();
            }
            String resultt = String.format("%.2f", result);

            try {
                tv_result.setText(resultt);
            } catch (Exception e) {
                e.printStackTrace();
                tv_result.setText(R.string.error);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        fromCurrency = fromSpinner.getItemAtPosition(position).toString();
        toCurrency = toSpinner.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public int turnCurrencyToPosition(String currency) {
        int position = -1;
        switch (currency) {
            case "AUD":
                position = 0;
                break;
            case "CAD":
                position = 1;
                break;
            case "CZK":
                position = 2;
                break;
            case "DKK":
                position = 3;
                break;
            case "HUF":
                position = 4;
                break;
            case "JPY":
                position = 5;
                break;
            case "NOK":
                position = 6;
                break;
            case "SEK":
                position = 7;
                break;
            case "CHF":
                position = 8;
                break;
            case "GBP":
                position = 9;
                break;
            case "USD":
                position = 10;
                break;
            case "BAM":
                position = 11;
                break;
            case "EUR":
                position = 12;
                break;
            case "PLN":
                position = 13;
                break;
        }
        return position;
    }
}