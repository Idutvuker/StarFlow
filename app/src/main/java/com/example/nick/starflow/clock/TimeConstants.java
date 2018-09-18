package com.example.nick.starflow.clock;

/**
 * Created by Nick on 23.03.2017.
 */

public final class TimeConstants {
    public static final long MILLIS_SECOND = 1000L;
    public static final long MILLIS_MINUTE = 60000L;
    public static final long MILLIS_HOUR = 3600000L;
    public static final long MILLIS_DAY = 24 * MILLIS_HOUR;

    public static final long MILLIS_JUL_YEAR = (MILLIS_DAY * 36525) / 100;
    public static final long MILLIS_TROP_YEAR = 31556745000L;

}
