package com.example.nick.starflow.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.nick.starflow.clock.Clock;
import com.example.nick.starflow.databases.DatabaseManager;
import com.example.nick.starflow.mainview.MainView;
import com.example.nick.starflow.mainview.MainViewListener;
import com.example.nick.starflow.R;
import com.example.nick.starflow.databases.types.StarData;
import com.example.nick.starflow.databases.AbbrevDatabase;
import com.example.nick.starflow.databases.CitiesDatabase;
import com.example.nick.starflow.databases.ConstellationDatabase;
import com.example.nick.starflow.databases.StarDatabase;
import com.example.nick.starflow.options.Options;
import com.example.nick.starflow.options.OptionsActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static com.example.nick.starflow.control.Constants.DATABASE_ABBREV_FILE;
import static com.example.nick.starflow.control.Constants.DATABASE_CITIES_FILE;
import static com.example.nick.starflow.control.Constants.DATABASE_CONSTELLATION_FILE;
import static com.example.nick.starflow.control.Constants.DATABASE_STAR_FILE;

public class MainActivity extends Activity {
    private MainView mainView;
    private Button button;

    private SearchView search;
    private ListView search_list;

    private AlertDialog.Builder builder;
    private MyMainViewListener listener = new MyMainViewListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Changing orientation to landscape
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            Log.d("Orientation", "Changing orientation to landscape");
            return;
        }

        if (loadDatabases()) {
            setContentView(R.layout.activity_main);

            //Options.sharedPrefs.edit().clear().apply();

            searchSetUp();

            button = (Button) findViewById(R.id.activeStarBtn);

            mainView = (MainView) findViewById(R.id.mainView);
            mainView.setListener(listener);

            datePickerSetUp();

            builder = new AlertDialog.Builder(this);
            sensorsSetUp();

            Options.setSharedPrefs(PreferenceManager.getDefaultSharedPreferences(this));
            Options.push();
        }

    }

    private static SensorManager sensorManager;
    private static Sensor gyroSensor;

    private void sensorsSetUp() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    private static float[] gyroData = new float[3];
    private static int mesN = 0;

    private static SensorEventListener sensListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event)
        {
            if (mesN > 5) {
                gyroData[0] = event.values[0];
                gyroData[1] = event.values[1];
                gyroData[2] = event.values[2];
            }

            mesN++;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private static boolean registered = false;

    public static void registerSensors()
    {
        if (!registered) {
            sensorManager.registerListener(sensListener, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
            registered = true;
        }
    }

    public static void unregisterSensors()
    {
        if (registered)
        {
            mesN = 0;

            gyroData[0] = 0;
            gyroData[1] = 0;
            gyroData[2] = 0;

            sensorManager.unregisterListener(sensListener, gyroSensor);
            registered = false;
        }
    }

    public static float[] getGyroData()
    {
        return gyroData;
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterSensors();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    private void searchSetUp()
    {
        search = (SearchView) findViewById(R.id.search);
        search_list = (ListView) findViewById(R.id.search_list);

        final MyAdapter adapter = new MyAdapter(this,
                android.R.layout.simple_list_item_1,
                DatabaseManager.starDatabase.getNamedStars());


        search_list.setAdapter(adapter);
        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                search.setQuery("", false);
                search.clearFocus();
                search.setIconified(true);

                listener.onFoundStar(adapter.getItem(position));
            }
        });

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    search_list.setVisibility(View.VISIBLE);
                else
                    search_list.setVisibility(View.GONE);
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    private Button dateButton;

    private View datePicker;
    private NumberPicker np_year;
    private NumberPicker np_month;
    private NumberPicker np_day;
    private NumberPicker np_hour;
    private NumberPicker np_minute;

    private final GregorianCalendar cal = new GregorianCalendar(Clock.UTC);

    private void datePickerSetUp() {
        dateButton = (Button) findViewById(R.id.dateButton);

        datePicker = findViewById(R.id.datePickerLayout);

        np_year = (NumberPicker) findViewById(R.id.np_year);
        np_month = (NumberPicker) findViewById(R.id.np_month);
        np_day = (NumberPicker) findViewById(R.id.np_day);
        np_hour = (NumberPicker) findViewById(R.id.np_hour);
        np_minute = (NumberPicker) findViewById(R.id.np_minute);

        final HashMap<NumberPicker, NumberPicker> hmap = new HashMap<>();

        hmap.put(np_minute, np_hour);
        hmap.put(np_hour, np_day);
        hmap.put(np_day, np_month);
        hmap.put(np_month, np_year);

        NumberPicker.OnValueChangeListener np_listener = new NumberPicker.OnValueChangeListener() {
            private void tWrap(NumberPicker picker, int i) {
                int x = picker.getValue() + i;
                picker.setValue(x);
                if (picker != np_year)
                    if (x > picker.getMaxValue() || x < picker.getMinValue())
                        tWrap(hmap.get(picker), i);

            }

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (picker != np_year) {
                    if (newVal == picker.getMaxValue() && oldVal == picker.getMinValue())
                        tWrap(hmap.get(picker), -1);

                    if (newVal == picker.getMinValue() && oldVal == picker.getMaxValue())
                        tWrap(hmap.get(picker), 1);
                }

                cal.set(np_year.getValue(), np_month.getValue() - 1, 1);
                np_day.setMaxValue(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

                cal.set(np_year.getValue(), np_month.getValue() - 1, np_day.getValue(),
                        np_hour.getValue(), np_minute.getValue());

                dateButton.setText(Clock.dateFormat.format(cal.getTime()));

                Clock.setTime(cal.getTime());
            }
        };

        np_year.setMinValue(2000);
        np_year.setMaxValue(2099);
        np_year.setWrapSelectorWheel(false);
        np_year.setOnValueChangedListener(np_listener);

        np_month.setMinValue(1);
        np_month.setMaxValue(12);
        np_month.setOnValueChangedListener(np_listener);

        np_day.setMinValue(1);
        np_day.setOnValueChangedListener(np_listener);

        np_hour.setMinValue(0);
        np_hour.setMaxValue(23);
        np_hour.setOnValueChangedListener(np_listener);

        np_minute.setMinValue(0);
        np_minute.setMaxValue(59);
        np_minute.setOnValueChangedListener(np_listener);

        datePickerSetCurTime();
    }

    private void datePickerSetCurTime() {
        Clock.setCurrentTime();
        cal.setTime(Clock.getTime());

        np_year.setValue(cal.get(Calendar.YEAR));
        np_month.setValue(cal.get(Calendar.MONTH) + 1);

        np_day.setMaxValue(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        np_day.setValue(cal.get(Calendar.DAY_OF_MONTH));

        np_hour.setValue(cal.get(Calendar.HOUR_OF_DAY));
        np_minute.setValue(cal.get(Calendar.MINUTE));

        dateButton.setText(Clock.dateFormat.format(cal.getTime()));
    }

    private boolean loadDatabases() {
        StarDatabase starDatabase = new StarDatabase();
        ConstellationDatabase constDatabase = new ConstellationDatabase();
        AbbrevDatabase abbrevDatabase = new AbbrevDatabase();
        CitiesDatabase citiesDatabase = new CitiesDatabase();

        try {
            DatabaseManager.loadFromAssets(starDatabase, this, DATABASE_STAR_FILE);
            DatabaseManager.starDatabase = starDatabase;

            DatabaseManager.loadFromAssets(abbrevDatabase, this, DATABASE_ABBREV_FILE);
            DatabaseManager.abbrevDatabase = abbrevDatabase;

            DatabaseManager.loadFromAssets(constDatabase, this, DATABASE_CONSTELLATION_FILE);
            DatabaseManager.constDatabase = constDatabase;

            DatabaseManager.loadFromAssets(citiesDatabase, this, DATABASE_CITIES_FILE);
            DatabaseManager.citiesDatabase = citiesDatabase;

            DatabaseManager.loaded = true;
            Log.d("Databases", "All databases are loaded!");


        } catch (IOException e) {
            e.printStackTrace();

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Can't load databases", Toast.LENGTH_LONG);
            toast.show();

            return false;
        }

        return true;
    }

    private void createStarInfoDialog() {
        //Set title to name or HYG database id
        builder.setTitle(activeStar.proper.isEmpty() ? "HYG" + activeStar.id : activeStar.proper);

        builder.setMessage(activeStar.printInfo());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.activeStarBtn:    //Star information button
                createStarInfoDialog();
                break;

            case R.id.dateButton:      //Date button
                if (datePicker.getVisibility() == View.VISIBLE)
                    datePicker.setVisibility(View.GONE);

                else
                    datePicker.setVisibility(View.VISIBLE);

                break;

            case R.id.curTimeButton:   //Set current time button
                datePickerSetCurTime();
                break;

            case R.id.optionsButton:
                Intent i = new Intent(MainActivity.this, OptionsActivity.class);
                startActivity(i);

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private Animation fadein = new AlphaAnimation(0.f, 1.f);
    private Animation fadeout = new AlphaAnimation(1.f, 0.f);
    {
        fadein.setDuration(500);
        fadein.setInterpolator(new DecelerateInterpolator());
        fadein.setFillBefore(true);

        fadeout.setFillAfter(true);
        fadeout.setDuration(500);
        fadeout.setInterpolator(new AccelerateInterpolator());
    }

    private StarData activeStar;

    private class MyMainViewListener extends MainViewListener
    {

        @Override
        public void onFoundStar(StarData star)
        {
            mainView.rotateToStar(star);
            updateActiveStarBtn(star);
        }

        @Override
        public void updateActiveStarBtn(StarData star) {
            if (activeStar != star) {
                mainView.activeStar = star;
                activeStar = star;

                button.setVisibility(View.VISIBLE);
                //button.startAnimation(fadein);
                if (star.proper.isEmpty())
                    button.setText("HYG" + star.id);
                else
                    button.setText(star.proper);
            }
        }

        @Override
        public void hideActiveStarBtn() {
            if (activeStar != null) {
                mainView.activeStar = null;
                activeStar = null;
                //button.startAnimation(fadeout);
                button.setVisibility(View.GONE);
            }
        }
    }
}
