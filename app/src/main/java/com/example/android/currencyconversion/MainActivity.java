package com.example.android.currencyconversion;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.currencyconversion.factories.ConvertFactory;
import com.example.android.currencyconversion.helpers.Helper;
import com.example.android.currencyconversion.models.ConvertInputs;
import com.example.android.currencyconversion.viewModels.ConvertViewModel;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    double result;

    private TextView tv_result;
    private EditText et_amount;
    private Button btn_convert_currency;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private RadioButton rbtn_buy;
    private ProgressBar pb_progress;

    ConvertViewModel viewModel;
    String fromCurrency;
    String toCurrency;
    Throwable t;
    String StringResultForShow = "";

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
        pb_progress = findViewById(R.id.pb_progress);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this, new ConvertFactory(getApplication())).get(ConvertViewModel.class);
    }

    // selected currencies from spinners
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapterView == fromSpinner) {
            fromCurrency = fromSpinner.getItemAtPosition(position).toString();
        } else if (adapterView == toSpinner) {
            toCurrency = toSpinner.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    // when "CONVERT" btn is pressed, amount, selected currencies and buying/selling rate are sent to
    // repository through viewmodel, network call is fetching new currency rates from hnb,
    // conversion is done and result is sent to ConvertResponseWrapper, then back to activity through viewmodel
    private void convertBtnPressed() {
        btn_convert_currency.setOnClickListener(view -> {
            pb_progress.setVisibility(View.VISIBLE);
            double amount;
            // if "amount" of currency for converting is not entered -> set to "1"
            if (et_amount.getText().toString().trim().isEmpty()) {
                amount = 1.0;
                et_amount.setText("1", TextView.BufferType.EDITABLE);
            } else {
                String amountWithCharRemoved = et_amount.getText().toString().trim().replace(",", "");
                amount = Double.parseDouble(amountWithCharRemoved);
                et_amount.setText(Helper.formatNumberWithThousandsSeparators(amount), TextView.BufferType.EDITABLE);
            }

            // send inputs for conversion to repository (amount, fromCurrency, toCurrency, rbtn_buy.isChecked()) -> and get result back
            ConvertInputs inputs = new ConvertInputs(amount, fromCurrency, toCurrency, rbtn_buy.isChecked());

            viewModel.getResult(inputs).observe(this, convertResponseWrapper -> {
                if (convertResponseWrapper == null) {
                    Helper.showAlertDialog(MainActivity.this, "Smth went wrong");
                    // Call is successful, show result
                } else if (convertResponseWrapper.getError() == null) {
                    result = convertResponseWrapper.getResult();
                    StringResultForShow = Helper.formatNumberWithThousandsSeparators(result);
                    tv_result.setText(StringResultForShow);
                    pb_progress.setVisibility(View.INVISIBLE);
                } else {
                    // Call failed
                    t = convertResponseWrapper.getError();
                    Helper.showAlertDialog(MainActivity.this, t.getMessage());
                }
            });
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("result", StringResultForShow);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        StringResultForShow = savedInstanceState.getString("result");
        tv_result.setText(StringResultForShow);
    }
}