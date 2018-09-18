package com.example.nick.starflow.clock;


/**
 * Created by Nick on 08.03.2017.
 */


public final class MyTimer {
    private static final double sec = 1000000000;

    private double prevTime;

    private static double getTime() // inline?
    {
        return System.nanoTime() / sec;
    }

    public void zero() {
        prevTime = 0;
    }

    public void start() {
        prevTime = getTime();
    }

    public double getElapsedTime() {
        double curTime = getTime();
        double r = curTime - prevTime;
        prevTime = curTime;

        return r;
    }
}
