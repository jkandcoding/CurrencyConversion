package com.example.android.currencyconversion;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.currencyconversion.factories.ConvertFactory;
import com.example.android.currencyconversion.models.ConvertInputs;
import com.example.android.currencyconversion.viewModels.ConvertViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.android.currencyconversion.helpers.Helper.formatNumberWithThousandsSeparators;
import static com.example.android.currencyconversion.helpers.Helper.showAlertDialog;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    double result;

    private TextView tv_amount_label;
    private TextView tv_result;
    private TextInputLayout et_amount_layout;
    private TextInputEditText et_amount_editText;
    private Button btn_convert_currency;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private RadioButton rbtn_buy;
    private ProgressBar pb_progress;

    private ConvertViewModel viewModel;
    private String fromCurrency;
    private String toCurrency;
    private Throwable t;
    private String StringResultForShow = "";
    private double amount;
    private String amountText;
    private String resultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewModel();
        initViews();
        setErrorOnTextInputEditText();
        convertBtnPressed();
        onKeyboardEnterPressed();

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
        tv_amount_label = findViewById(R.id.tv_amount_label);
        btn_convert_currency = findViewById(R.id.btn_convert_currency);
        et_amount_layout = findViewById(R.id.et_currency_layout);
        et_amount_editText = findViewById(R.id.et_currency_editText);
        rbtn_buy = findViewById(R.id.rbtn_buying_rate);
        pb_progress = findViewById(R.id.pb_progress);

        amountText = getResources().getString(R.string.amountText);
        resultText = getResources().getString(R.string.resultText);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this, new ConvertFactory(getApplication())).get(ConvertViewModel.class);
    }

    private void setErrorOnTextInputEditText() {
        et_amount_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > et_amount_layout.getCounterMaxLength()) {
                    et_amount_layout.setError(getString(R.string.max_char_length_is) + et_amount_layout.getCounterMaxLength());
                } else {
                    et_amount_layout.setError(null);
                }
            }
        });
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
        btn_convert_currency.setOnClickListener(view -> doConversion());
    }

    private void doConversion() {
        //don't perform conversion if et_amount_layout.getEditText().getText() length > et_amount_layout.getCounterMaxLength()
        if (et_amount_layout.getEditText().getText().toString().trim().length() <= et_amount_layout.getCounterMaxLength()) {

            pb_progress.setVisibility(View.VISIBLE);
            // if "amount" of currency for converting is not entered -> set to "1"
            if (et_amount_layout.getEditText().getText().toString().trim().isEmpty()) {
                amount = 1.0;
                et_amount_layout.getEditText().setText("1", TextView.BufferType.EDITABLE);
            } else {
                String amountWithCharRemoved = et_amount_layout.getEditText().getText().toString().trim().replace(",", "");
                amount = Double.parseDouble(amountWithCharRemoved);
            }
            tv_amount_label.setText(String.format(amountText, formatNumberWithThousandsSeparators(amount), fromCurrency));

            // send inputs for conversion to repository (amount, fromCurrency, toCurrency, rbtn_buy.isChecked()) -> and get result back
            ConvertInputs inputs = new ConvertInputs(amount, fromCurrency, toCurrency, rbtn_buy.isChecked());

            viewModel.getResult(inputs).observe(this, convertResponseWrapper -> {
                if (convertResponseWrapper == null) {
                    showAlertDialog(MainActivity.this, getString(R.string.smth_went_wrong));
                    // Call is successful, show result
                } else if (convertResponseWrapper.getError() == null) {
                    result = convertResponseWrapper.getResult();
                    StringResultForShow = formatNumberWithThousandsSeparators(result);
                    tv_result.setText(String.format(resultText, StringResultForShow, toCurrency));
                    pb_progress.setVisibility(View.INVISIBLE);
                } else {
                    // Call failed
                    t = convertResponseWrapper.getError();
                    showAlertDialog(MainActivity.this, t.getMessage());
                }
            });
        }
    }

    private void onKeyboardEnterPressed() {
        et_amount_editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                doConversion();
            }
            return false;
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble("amount", amount);
        savedInstanceState.putString("result", StringResultForShow);
        savedInstanceState.putString("fromCurrency", fromCurrency);
        savedInstanceState.putString("toCurrency", toCurrency);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        amount = savedInstanceState.getDouble("amount");
        StringResultForShow = savedInstanceState.getString("result");
        fromCurrency = savedInstanceState.getString("fromCurrency");
        toCurrency = savedInstanceState.getString("toCurrency");
        tv_amount_label.setText(String.format(amountText, formatNumberWithThousandsSeparators(amount), fromCurrency));
        tv_result.setText(String.format(resultText, StringResultForShow, toCurrency));
    }
}