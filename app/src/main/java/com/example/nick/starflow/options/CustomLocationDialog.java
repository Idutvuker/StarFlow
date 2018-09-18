package com.example.nick.starflow.options;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.nick.starflow.R;
import com.example.nick.starflow.options.Options;

import org.joml.Math;

/**
 * Created by Nick on 26.06.2017.
 */

public class CustomLocationDialog extends DialogFragment
{
    private EditText lon_pick;
    private EditText lat_pick;
    private Button apl_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().setTitle("Custom location");
        View view = inflater.inflate(R.layout.location_picker, null);

        lon_pick = (EditText) view.findViewById(R.id.lat_pick);
        Log.d("abcd", "Lon: " + Options.sharedPrefs.getFloat("lon", 0.f));
        lon_pick.setText(String.valueOf(Options.sharedPrefs.getFloat("lon", 0)));

        lat_pick = (EditText) view.findViewById(R.id.lon_pick);
        Log.d("abcd", "Lat: " + Options.sharedPrefs.getFloat("lat", 0.f));
        lat_pick.setText(String.valueOf(Options.sharedPrefs.getFloat("lat", 0)));

        apl_button = (Button) view.findViewById(R.id.apl_button);
        apl_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private static float clamp(float a, float mn, float mx)
    {
        return Math.min(Math.max(a, mn), mx);
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        SharedPreferences.Editor ed = Options.sharedPrefs.edit();

        float lon = clamp(Float.valueOf(String.valueOf(lon_pick.getText())), -90, 90);
        float lat = clamp(Float.valueOf(String.valueOf(lat_pick.getText())), -180, 180);

        ed.putFloat("lon", lon);
        ed.putFloat("lat", lat);
        ed.apply();

        //Log.d("abcd", String.valueOf(Options.sharedPrefs.getFloat("lon", 0.f)));

        super.onDismiss(dialog);
    }
}
