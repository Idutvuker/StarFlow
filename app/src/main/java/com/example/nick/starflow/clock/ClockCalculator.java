package com.example.nick.starflow.clock;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.nick.starflow.clock.TimeConstants.MILLIS_DAY;
import static com.example.nick.starflow.clock.TimeConstants.MILLIS_TROP_YEAR;

/**
 * Created by Nick on 23.03.2017.
 */

public final class ClockCalculator
{
    static private GregorianCalendar VER_EQUINOX =
            new GregorianCalendar(Clock.UTC);

    static private GregorianCalendar J2000 =
            new GregorianCalendar(Clock.UTC);

    static {
        J2000.set(2000, Calendar.JANUARY, 1, 12, 0);
        VER_EQUINOX.set(2000, Calendar.MARCH, 20, 7, 35);
    }

    public static long getTimeSinceEpoch(Date date)
    {
        //Clock.printDate(J2000.getTime());
        return date.getTime() - J2000.getTimeInMillis();
    }

    public static long getLocalYearTime(Date date) {
        return (getTimeSinceEpoch(date) % MILLIS_TROP_YEAR) / MILLIS_TROP_YEAR;
    }

    public static double getJulianDate(GregorianCalendar date)
    {
        GregorianCalendar st = new GregorianCalendar(4713, Calendar.JANUARY, 1, 12, 0);
        st.set(GregorianCalendar.ERA, GregorianCalendar.BC);

        return (date.getTimeInMillis() - st.getTimeInMillis()) / (double) MILLIS_DAY;
    }

    public static double getSiderealTime(Date date)
    {
        double days = (getTimeSinceEpoch(date) / (double) MILLIS_DAY);
        //Log.d("DATE", ""+ days);
        double GST = 18.697374558 + 24.06570982441908 * days;

        Log.d("Clock", "Sidereal:" +GST%24);

        return GST % 24;
    }

    //static private GregorianCalendar tmp = new GregorianCalendar();

    /*
    public static void getNextEquinox()
    {
        Log.d("DATE", "SIDEREAL: " + getSiderealTime(new Date(System.currentTimeMillis())));

        tmp.setTime(new Date(VER_EQUINOX.getTimeInMillis() + 3 * MILLIS_TROP_YEAR));

        /*Log.d("DATE", "EQNX " + tmp.get(Calendar.YEAR) + " " +
                tmp.get(Calendar.MONTH) + " " +
                tmp.get(Calendar.DAY_OF_MONTH) + " " +
                tmp.get(Calendar.HOUR_OF_DAY) + ":" +
                tmp.get(Calendar.MINUTE)
        );
    }
    */
}
