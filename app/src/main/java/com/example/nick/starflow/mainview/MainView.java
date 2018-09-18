package com.example.nick.starflow.mainview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.nick.starflow.databases.DatabaseManager;
import com.example.nick.starflow.render.MyRenderer;
import com.example.nick.starflow.clock.MyTimer;
import com.example.nick.starflow.databases.types.StarData;

import org.joml.Vector2f;

import static com.example.nick.starflow.control.Constants.QUICK_TOUCH_MIN_ALPHA;
import static com.example.nick.starflow.control.Constants.QUICK_TOUCH_RADIUS;
import static com.example.nick.starflow.control.Constants.QUICK_TOUCH_TIME;

/**
 * Created by Nick on 01.02.2017.
 */
public class MainView extends SurfaceView implements SurfaceHolder.Callback, SurfaceView.OnTouchListener {
    private float curHeight;
    private float curWidth;

    private MyRenderer renderer;
    private DrawThread drawThread;

    private ScaleGestureDetector scaleGestDetector;


    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);

        scaleGestDetector = new ScaleGestureDetector(context, new MyScaleListener(this));

    }

    public void scaleCameraFov(float fov_deg) {
        renderer.camera.scaleFov(fov_deg);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        renderer = new MyRenderer(this);

        drawThread = new DrawThread(this, renderer);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        curWidth = width;
        curHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.setRunning(false);

        while (true) {
            try {
                drawThread.join();
                break;
            } catch (InterruptedException e) {
            }
        }
    }

    private Vector2f prevPos = new Vector2f();
    private MyTimer timer = new MyTimer();
    private MyTimer qTouchTimer = new MyTimer();

    private boolean onScaleGesture;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestDetector.onTouchEvent(event);

        if (scaleGestDetector.isInProgress())
            onScaleGesture = true;

        else if (onScaleGesture && event.getAction() == MotionEvent.ACTION_DOWN) {
            onScaleGesture = false;
        }

        if (!onScaleGesture) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    timer.start();
                    qTouchTimer.start();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float dx = (prevPos.x - x) / curWidth;
                    float dy = (prevPos.y - y) / curHeight;

                    if (Math.abs(dx) > 0.01 || Math.abs(dy) > 0.01)
                        qTouchTimer.zero(); //Invalidate timer

                    drawThread.renderer.camera.touchRotate(dx, dy, timer.getElapsedTime());

                    break;

                case MotionEvent.ACTION_UP:
                    if (qTouchTimer.getElapsedTime() <= QUICK_TOUCH_TIME)
                        starTouch(x, y);
                    break;

                case MotionEvent.ACTION_CANCEL:
            }

            prevPos.set(x, y);
        }


        return true;
    }

    private Vector2f vec2f = new Vector2f();

    public StarData activeStar;

    private boolean starTouch(float tx, float ty) {
        for (StarData star : DatabaseManager.starDatabase) {
            if (renderer.pointInView(star.viewpos)) {
                vec2f.set(star.viewpos.x - tx, star.viewpos.y - ty);

                //if ((Math.abs(vec2f.x) + Math.abs(vec2f.y)) / curHeight < QUICK_TOUCH_RADIUS


                if (vec2f.length() / curHeight < QUICK_TOUCH_RADIUS &&
                        star.alpha > QUICK_TOUCH_MIN_ALPHA)
                {
                    listener.updateActiveStarBtn(star);

                    return true;
                }
            }
        }

        listener.hideActiveStarBtn();

        return false;
    }

    private MainViewListener listener;

    public void setListener(MainViewListener listener) {
        this.listener = listener;
    }

    public void rotateToStar(StarData star) {
        renderer.camera.rotateTo(star.position);
    }
}
