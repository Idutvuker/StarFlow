package com.example.nick.starflow.control;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Nick on 26.02.2017.
 */

public final class Constants
{
    public static final String DATABASE_STAR_FILE = "StarDatabaseFinal.csv";
    public static final String DATABASE_ABBREV_FILE = "ConstellationAbbrevs.csv";
    public static final String DATABASE_CONSTELLATION_FILE = "ConstDatabaseFinal.dat";
    public static final String DATABASE_CITIES_FILE = "CitiesFinal.csv";

    public static final float FOV_MIN = 5.f;
    public static final float FOV_MAX = 90.f;
    public static final float FOV_DEF = 80.f;

    public static final float CAM_DAMPING = 0.04f;
    public static final float CAM_MAXVEL = 10.0f; // rad per sec

    public static final int STAR_MIN_SIZE = 3;
    public static final int STAR_MAX_SIZE = 13;
    public static final float STAR_DEF_MAG = 2.5f;

    public static final float SCALE_GESTURE_SENS = 1.1f;

    public static final float QUICK_TOUCH_TIME = 0.15f;
    public static final float QUICK_TOUCH_RADIUS = 0.07f;
    public static final int QUICK_TOUCH_MIN_ALPHA = 10;

    public static final Date CLOCK_MAX_DATE = new GregorianCalendar(2099, Calendar.JANUARY, 1, 12, 0).getTime();
    public static final Date CLOCK_MIN_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 1, 12, 0).getTime();

    public static final float TEXT_SMALL_STAR = 20.f;
    public static final float TEXT_BIG_STAR = 30.f;

    public static final int PREFERENCES_MAX_WIDTH = 1100;

    public static final float TEXT_CARD_PTS_SIZE = 40.f;
    public static final int TEXT_HORIZON_COLOR = 0xFF009999;
}
