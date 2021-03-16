package com.example.android.currencyconversion.helpers;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.android.currencyconversion.R;

public class Helper {
    public static void showAlertDialog(Context context, String errorMsg) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(context.getString(R.string.error));
        dialog.setMessage(errorMsg);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ok), (dialogInterface, i) -> dialog.dismiss());
        dialog.show();
    }
}
