package com.example.nick.starflow.clock;

import android.util.Log;

import com.example.nick.starflow.control.Constants;
import com.example.nick.starflow.control.EarthRotControl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Nick on 21.06.2017.
 */

public final class Clock {
    //UTC-0 time

    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    private static GregorianCalendar cal = new GregorianCalendar(UTC);

    public static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy.MM.dd HH:mm z", Locale.ENGLISH);

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void printDate(Date date)
    {
        Log.d("Clock", "Date: " + dateFormat.format(date));
    }


    public static void setCurrentTime()
    {
        cal.setTimeInMillis(System.currentTimeMillis());
        EarthRotControl.updateOrbitalMatrix();
    }

    public static boolean setTime(Date time)
    {
        //If date is out of bounds
        if (time.after(Constants.CLOCK_MAX_DATE) || time.before(Constants.CLOCK_MIN_DATE))
            return false;

        cal.setTime(time);

        EarthRotControl.updateOrbitalMatrix();

        return true;
    }

    public static Date getTime() {
        return cal.getTime();
    }

}
