package com.example.nick.starflow.mainview;

import android.view.ScaleGestureDetector;

import static com.example.nick.starflow.control.Constants.SCALE_GESTURE_SENS;

/**
 * Created by Nick on 11.04.2017.
 */

//Scale gesture listener
public class MyScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    private MainView mainView;

    public MyScaleListener(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mainView.scaleCameraFov((float) Math.pow(detector.getScaleFactor(), -SCALE_GESTURE_SENS));
        return true;
    }
}
