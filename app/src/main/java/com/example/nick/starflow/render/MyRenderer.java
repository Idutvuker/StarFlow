package com.example.nick.starflow.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Pair;

import com.example.nick.starflow.control.Constants;
import com.example.nick.starflow.R;
import com.example.nick.starflow.control.EarthRotControl;
import com.example.nick.starflow.databases.DatabaseManager;
import com.example.nick.starflow.databases.types.Constellation;
import com.example.nick.starflow.databases.types.StarData;
import com.example.nick.starflow.mainview.MainView;

import org.joml.Vector3f;

import static com.example.nick.starflow.control.Constants.STAR_DEF_MAG;
import static com.example.nick.starflow.control.Constants.TEXT_BIG_STAR;
import static com.example.nick.starflow.control.Constants.TEXT_CARD_PTS_SIZE;
import static com.example.nick.starflow.control.Constants.TEXT_HORIZON_COLOR;
import static com.example.nick.starflow.control.Constants.TEXT_SMALL_STAR;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static org.joml.Math.PI;


/**
 * Created by Nick on 09.02.2017.
 */

public class MyRenderer {
    public volatile MyCamera camera;

    //private DatabaseManager dtbMng;
    private MainView surface;

    private float curWidth;
    private float curHeight;

    private final int URES = 24;
    private final int VRES = 15;

    private static final Paint paint = new Paint();
    private static final Paint fontPaint = new Paint();
    private static final Paint conPaint = new Paint();
    private static final Paint cirPaint = new Paint();

    {
        fontPaint.setColor(Color.WHITE);
        fontPaint.setStyle(Paint.Style.FILL);
        fontPaint.setTextAlign(Paint.Align.CENTER);

        conPaint.setTextSize(40.f);
        conPaint.setColor(Color.GRAY);
        conPaint.setStrokeWidth(2);

        cirPaint.setStrokeWidth(3);
        cirPaint.setTextSize(50.f);
        cirPaint.setTextAlign(Paint.Align.CENTER);
    }

    public MyRenderer(MainView surface) {
        this.surface = surface;

        curWidth = surface.getWidth();
        curHeight = surface.getHeight();

        //Load star image
        starBitmap = ContextCompat.getDrawable(surface.getContext(), R.drawable.star);

        genSphere(URES, VRES);

        camera = new MyCamera(curWidth / curHeight, (float) toRadians(Constants.FOV_DEF));
    }

    public static void enableAntialiasing(boolean flag) {
        fontPaint.setAntiAlias(flag);
        paint.setAntiAlias(flag);
        conPaint.setAntiAlias(flag);
        cirPaint.setAntiAlias(flag);
    }

    private Vector3f[][] sphereVertices;
    private Vector3f[][] gridProjection;

    private void genSphere(int ures, int vres) {
        sphereVertices = new Vector3f[ures][vres];
        gridProjection = new Vector3f[ures][vres];

        float stepU = 2 * (float) PI / (ures);
        float stepV = (float) PI / (vres - 1);

        for (int u = 0; u < ures; u++) {
            float beta = u * stepU;
            for (int v = 0; v < vres; v++) {
                float alpha = v * stepV;
                sphereVertices[u][v] = new Vector3f(
                        -(float) (sin(alpha) * cos(beta)),
                        (float) (sin(alpha) * sin(beta)),
                        (float) (cos(alpha))
                );
                gridProjection[u][v] = new Vector3f();
            }
        }
    }

    private Vector3f vec1 = new Vector3f();
    private Vector3f vec2 = new Vector3f();

    public boolean pointInView(Vector3f v1) {
        if (v1.z >= 0)
            return false;

        if (v1.x > curWidth)
            return false;

        if (v1.x < 0)
            return false;

        if (v1.y > curHeight)
            return false;

        if (v1.y < 0)
            return false;

        return true;
    }

    public boolean lineInView(Vector3f v1, Vector3f v2) {
        if (v1.z >= 0 || v2.z >= 0)
            return false;

        if (v1.x > curWidth && v2.x > curWidth)
            return false;

        if (v1.x < 0 && v2.x < 0)
            return false;

        if (v1.y > curHeight && v2.y > curHeight)
            return false;

        if (v1.y < 0 && v2.y < 0)
            return false;

        return true;
    }

    public void drawSphereGrid(Canvas canvas) {
        paint.setColor(Color.argb(200, 100, 100, 100));

        for (int i = 0; i < URES; i++)        //Preprojection of points
            for (int j = 0; j < VRES; j++) {
                vec1.set(sphereVertices[i][j]);
                toCam(vec1);
                EarthRotControl.localTransform(vec1);
                toView(vec1);

                gridProjection[i][j].set(vec1);
            }

        for (int i = 0; i < URES; i++) {
            for (int j = 0; j < VRES - 1; j++) {
                vec1.set(gridProjection[i][j]);
                vec2.set(gridProjection[i][j + 1]);

                if (lineInView(vec1, vec2))
                    canvas.drawLine(vec1.x, vec1.y, vec2.x, vec2.y, paint);

                vec2.set(gridProjection[(i + 1) % (URES)][j]);

                if (lineInView(vec1, vec2))
                    canvas.drawLine(vec1.x, vec1.y, vec2.x, vec2.y, paint);
            }
        }


        //Draw horizon circle
        vec2.set(sphereVertices[0][VRES/2]);
        toCam(vec2);
        toView(vec2);

        cirPaint.setTextSize(min(max(alpha / 5.f, 15), 35));

        for (int i = 1; i < URES + 1; i++)
        {
            vec1.set(sphereVertices[i%URES][VRES/2]);
            toCam(vec1);
            toView(vec1);

            if (pointInView(vec1))
            {
                canvas.drawText(String.valueOf(360 / URES * i),
                        vec1.x, vec1.y + cirPaint.getTextSize(), cirPaint);


                cirPaint.setColor(TEXT_HORIZON_COLOR);
                canvas.drawLine(vec1.x, vec1.y - 5, vec1.x, vec1.y + 5, cirPaint);
            }

            if (pointInView(vec1) || pointInView(vec2))
            {
                cirPaint.setColor(0x5500FFFF);
                canvas.drawLine(vec1.x, vec1.y, vec2.x, vec2.y, cirPaint);
            }

            vec2.set(vec1);
        }

        String[] cardp = {"North", "West", "South", "East"}; //Cardinal points

        float d = (float) PI / 2.f;

        int tmpcol = cirPaint.getColor();
        cirPaint.setColor(0xFF009999);
        cirPaint.setTextSize(TEXT_CARD_PTS_SIZE);

        for (int i = 0; i < 4; i++)
        {
            vec1.set((float) sin(d * i), 0, (float) cos(d*i));
            toView(vec1);
            if (pointInView(vec1))
            {
                canvas.drawText(cardp[i], vec1.x, vec1.y - 8, cirPaint);
                canvas.drawCircle(vec1.x, vec1.y, 6, cirPaint);
                //canvas.drawLine(vec1.x, vec1.y - 8, vec1.x, vec1.y + 8, cirPaint);
            }
        }

        cirPaint.setColor(tmpcol);

    }

    public static Vector3f toCam(Vector3f vec) {
        float t = vec.x;
        vec.x = -vec.y;
        vec.y = vec.z;
        vec.z = -t;
        return vec;
    }

    private Vector3f toView(Vector3f vec) {
        camera.project(vec);
        vec.x = (vec.x + 1) * (curWidth / 2);
        vec.y = (1 - vec.y) * (curHeight / 2);
        return vec;
    }

    //Fade factor
    private int alpha;

    // DRAWING
    public void onDraw(Canvas canvas, double elapsedTime) {
        camera.dampRotation(elapsedTime);

        float x = camera.getFovValue();

        //Background color
        int col = Color.argb(255, (int) (20 * x), (int) (20 * x), (int) (35 * x)) + 0x000023;
        canvas.drawColor(col);

        //Fade factor
        alpha = (int) (0XFF * ((1 - x)));

        drawSphereGrid(canvas);
        drawStarMap(canvas);
        drawConstellationLines(canvas);

        //Draw fps
        //canvas.drawText(String.valueOf(round(1.0/elapsedTime)), 50, 30, fontPaint);
    }

    private void drawConstellationLines(Canvas canvas) {

        //Different constellations are drawn with different colors
        int dcol = 150 / DatabaseManager.constDatabase.size(); //Color change per constellation
        int scol = Color.rgb(160, 160, 160);                   //Start color

        for (Constellation con : DatabaseManager.constDatabase) {

            //Average coordinates
            float ax = 0;
            float ay = 0;

            int n = 0;

            boolean flag = false;

            for (Pair<Integer, Integer> p : con) {
                StarData f = DatabaseManager.starDatabase.get(p.first);
                StarData s = DatabaseManager.starDatabase.get(p.second);

                conPaint.setColor(scol);
                conPaint.setAlpha((int) min(max(alpha * 1.5f, 0), 0x88));

                if (lineInView(f.viewpos, s.viewpos)) {
                    canvas.drawLine(f.viewpos.x, f.viewpos.y, s.viewpos.x, s.viewpos.y, conPaint);
                }

                if (f.viewpos.z < 0) {
                    ax += f.viewpos.x;
                    ay += f.viewpos.y;
                    n++;
                }
                if (s.viewpos.z < 0) {
                    ax += s.viewpos.x;
                    ay += s.viewpos.y;
                    n ++;
                }

            }

            //Draw constellation name
            if (n > 1) {
                canvas.drawText(con.name, ax / n, ay / n, conPaint);
            }

            //Change color
            scol += dcol * 256 + dcol;
        }
    }

    private void drawStarMap(Canvas canvas) {
        for (StarData star : DatabaseManager.starDatabase) {
            drawStar(canvas, star);
        }
    }

    private Drawable starBitmap;
    private Rect dst = new Rect();

    private void drawStar(Canvas canvas, StarData star) {
        vec1.set(star.position);

        toCam(vec1);
        EarthRotControl.transform(vec1);
        toView(vec1);

        star.viewpos.set(vec1);

        if (vec1.z >= 0)
            return;

        int SZ = star.px_size;
        dst.set((int) vec1.x - SZ, (int) vec1.y - SZ, (int) vec1.x + SZ, (int) vec1.y + SZ);

        int col_a = 0xFF;

        //Draw star image if star is bright else draw circle
        if (star.apmag < STAR_DEF_MAG) {
            starBitmap.setBounds(dst);
            starBitmap.setColorFilter(star.color, PorterDuff.Mode.MULTIPLY);
            starBitmap.draw(canvas);
        } else {
            paint.setColor(star.color);
            col_a = max(min(alpha * 2 - 70, 0xFF), 0);
            paint.setAlpha(col_a);
            star.alpha = col_a;
            canvas.drawCircle(vec1.x, vec1.y, SZ, paint);
        }

        //Draw active star selection
        if (star == surface.activeStar)
        {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xFFFF0000);
            paint.setStrokeWidth(4);

            canvas.drawCircle(vec1.x, vec1.y, SZ + 5, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(1);
        }

        //Draw star name
         if (!star.proper.isEmpty())
        {
            if (star.apmag > STAR_DEF_MAG) {
                fontPaint.setTextSize(TEXT_SMALL_STAR);
            }

            else {
                fontPaint.setTextSize(TEXT_BIG_STAR);
            }

            fontPaint.setAlpha(col_a);

            canvas.drawText(String.valueOf(star.proper),
                    vec1.x, vec1.y + fontPaint.getTextSize(), fontPaint);
        }
    }
}
