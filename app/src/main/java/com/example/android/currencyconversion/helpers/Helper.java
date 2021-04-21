package com.example.android.currencyconversion.helpers;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.android.currencyconversion.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Helper {

    public static void showAlertDialog(Context context, String errorMsg) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(context.getString(R.string.error));
        dialog.setMessage(errorMsg);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ok), (dialogInterface, i) -> dialog.dismiss());
        dialog.show();
    }

    public static String formatNumberWithThousandsSeparators (double number) {
        //DecimalFormat myFormatter = new DecimalFormat("###,###.##");
        DecimalFormat myFormatter = new DecimalFormat("###,###.##");
        return myFormatter.format(number);
    }

}
