package com.example.nick.starflow.options;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.nick.starflow.databases.types.City;
import com.example.nick.starflow.databases.DatabaseManager;
import com.example.nick.starflow.control.LocationControl;
import com.example.nick.starflow.activity.MainActivity;
import com.example.nick.starflow.render.MyRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 26.06.2017.
 */

public final class Options
{
    public static SharedPreferences sharedPrefs;

    public static boolean sphmode = false;
    public static boolean gyrosens = false;

    public static Map<String, Object> map = new HashMap<>();

    public static void setSharedPrefs(SharedPreferences sharedPrefs) {
        Options.sharedPrefs = sharedPrefs;
    }

    public static void push()
    {
        Log.d("Options", "PUSH");
        if (sharedPrefs.getBoolean("cust_location", false))
        {
            Log.d("Options", "Custom location used");
            Log.d("Options", "Longitude:" + Options.sharedPrefs.getFloat("lon", 0.f));
            Log.d("Options", "Latitude:" + Options.sharedPrefs.getFloat("lat", 0.f));

            map.put("latitude", sharedPrefs.getFloat("lat", 0.f));
            map.put("longitude", sharedPrefs.getFloat("lon", 0.f));
        }

        else
        {
            Log.d("Options", "City location used");

            int n = Integer.valueOf(sharedPrefs.getString("city_pick", "0"));
            City c = DatabaseManager.citiesDatabase.get(n);

            Log.d("Options", "City: "+c.name);

            Log.d("Options", "Latitude:" + c.lat);
            Log.d("Options", "Longitude:" + c.lon);

            map.put("latitude", c.lat);
            map.put("longitude", c.lon);
        }

        LocationControl.updateLocation();

        sphmode = sharedPrefs.getBoolean("sphmode", false);

        MyRenderer.enableAntialiasing(sharedPrefs.getBoolean("antialias", true));

        boolean newgyro = sharedPrefs.getBoolean("gyrosens", false);

        if (!gyrosens && newgyro)
        {
            MainActivity.registerSensors();
        }

        else if (gyrosens && !newgyro)
        {
            MainActivity.unregisterSensors();
        }

        gyrosens = newgyro;
    }
}
