package com.example.nick.starflow.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Nick on 12.04.2017.
 */

public class StarInfoDialog extends DialogFragment {
    private View view;
    private AlertDialog.Builder builder;

    public StarInfoDialog() {
        builder = new AlertDialog.Builder(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return builder.create();
    }
}
