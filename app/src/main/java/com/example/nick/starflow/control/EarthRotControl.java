package com.example.nick.starflow.control;

import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.example.nick.starflow.clock.Clock;
import com.example.nick.starflow.clock.ClockCalculator;

import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

/**
 * Created by Nick on 03.04.2017.
 */


public final class EarthRotControl {
    private static final float e_rad = 0.4090928f;

    private static Matrix3f orbitalMatrix = new Matrix3f();
    private static Matrix3f locationMatrix = new Matrix3f();


    public static Vector3f localTransform(Vector3f vec)
    {
        return locationMatrix.transform(vec);
    }

    public static Vector3f transform(Vector3f vec)
    {
        orbitalMatrix.transform(vec);
        locationMatrix.transform(vec);
        return vec;
    }

    public static void updateLocationMatrix()
    {
        float lon = (float) toRadians(LocationControl.getLongitude());
        float lat = (float) toRadians(LocationControl.getLatitude());

        locationMatrix.identity();

        locationMatrix.rotateX(-lat + (float) toRadians(90));
        locationMatrix.rotateY(-lon);
    }

    private static float curphi;

    private static class ValueChanger {
        static Timer timer = new Timer();

        static final int delay = 10;
        static final int period = 300;

        volatile static int beg = 0;
        volatile static float from;
        volatile static float to;

        static Interpolator intr = new DecelerateInterpolator();

        private static TimerTask task = new TimerTask() {
            float x;
            float t;

            @Override
            public void run() {
                if (beg < period) {

                    beg += delay;

                    x = intr.getInterpolation((float) beg / period);

                    t = (to-from);

                    if (abs(t) > PI)
                        t = -signum(t)*(2 * (float) PI - abs(t));

                    curphi = (from + t * x) % (2 * (float) PI);

                    orbitalMatrix.rotationY(-curphi);
                }
            }
        };

        static {
            timer.scheduleAtFixedRate(task, 0, delay);
        }

        public static void start(float _from, float _to)
        {
            beg = 0;
            from = _from;
            to = _to;
        }
    }

    public static void updateOrbitalMatrix()
    {
        //Day rotation
        float phi = (float) ClockCalculator.getSiderealTime(Clock.getTime()) / 24 * 2 * (float) PI;

        ValueChanger.start(curphi, phi);

        //orbitalMatrix.rotationY(-phi);
    }


}
