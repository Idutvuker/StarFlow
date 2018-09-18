package com.example.nick.starflow.options;

import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.ViewGroup;
import android.view.Window;

import com.example.nick.starflow.control.Constants;
import com.example.nick.starflow.databases.DatabaseManager;
import com.example.nick.starflow.R;

public class OptionsActivity extends PreferenceActivity
{

    public static class PrefFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Window window =  getWindow();

        window.setLayout(Constants.PREFERENCES_MAX_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);

        android.app.FragmentManager fm = getFragmentManager();

        PrefFragment pf = new PrefFragment();


        fm.beginTransaction().replace(android.R.id.content, pf).commit();

        fm.executePendingTransactions();

        final ListPreference listpre = (ListPreference) pf.findPreference("city_pick");

        listpre.setEntries(DatabaseManager.citiesDatabase.getEntries());
        listpre.setEntryValues(DatabaseManager.citiesDatabase.getIds());

        final CheckBoxPreference checkPre = (CheckBoxPreference) pf.findPreference("cust_location");

        if (checkPre.isChecked()) {
            listpre.setEnabled(false);
        }

        Preference.OnPreferenceClickListener listener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                if (preference.getKey().equals("cust_location"))
                    listpre.setEnabled(!checkPre.isChecked());

                else {
                    DialogFragment dia = new CustomLocationDialog();
                    dia.show(getFragmentManager(), "loc_dia");
                }
                return false;
            }
        };

        checkPre.setOnPreferenceClickListener(listener);

        Preference preference = pf.findPreference("pick_loc");
        preference.setOnPreferenceClickListener(listener);
    }

    @Override
    protected void onDestroy() {
        Options.push();
        super.onDestroy();
    }
}
