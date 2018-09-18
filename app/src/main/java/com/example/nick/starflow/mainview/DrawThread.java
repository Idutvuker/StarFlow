package com.example.nick.starflow.mainview;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.nick.starflow.render.MyRenderer;
import com.example.nick.starflow.clock.MyTimer;

/**
 * Created by Nick on 04.03.2017.
 */

public class DrawThread extends Thread
{
    private boolean running = false;
    private SurfaceView surface;
    private SurfaceHolder holder;
    public MyRenderer renderer;

    public DrawThread(SurfaceView surface, MyRenderer renderer)
    {
        this.surface = surface;
        this.holder = surface.getHolder();
        this.renderer = renderer;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }


    @Override
    public void run() {
        Canvas canvas;

        MyTimer timer = new MyTimer();

        timer.start();

        while (running)
        {
            double elapsedTime = timer.getElapsedTime();

            canvas = null;

            try {
                canvas = holder.lockCanvas();

                if (canvas == null)
                    continue;

                renderer.onDraw(canvas, elapsedTime);

            } finally {
                if (canvas != null)
                {
                    holder.unlockCanvasAndPost(canvas);
                }

                //prevTime = curTime;
            }
        }
    }
}
