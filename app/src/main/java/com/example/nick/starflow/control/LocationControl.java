package com.example.nick.starflow.control;

import com.example.nick.starflow.options.Options;

/**
 * Created by Nick on 23.06.2017.
 */

public final class LocationControl
{
    public static void updateLocation() {
        EarthRotControl.updateLocationMatrix();
    }

    public static float getLongitude() {
        return (float) Options.map.get("longitude");
    }

    public static float getLatitude() {
        return (float) Options.map.get("latitude");
    }
}
