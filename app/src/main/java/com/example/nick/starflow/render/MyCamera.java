package com.example.nick.starflow.render;

import com.example.nick.starflow.control.Constants;
import com.example.nick.starflow.activity.MainActivity;
import com.example.nick.starflow.control.EarthRotControl;
import com.example.nick.starflow.options.Options;

import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.example.nick.starflow.control.Constants.FOV_MAX;
import static com.example.nick.starflow.control.Constants.FOV_MIN;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.signum;
import static java.lang.Math.tan;

/**
 * Created by Nick on 07.02.2017.
 */
public class MyCamera {
    private static final float FOV_MAX_RAD = (float) Math.toRadians(FOV_MAX);
    private static final float FOV_MIN_RAD = (float) Math.toRadians(FOV_MIN);

    private Matrix3f rotationMatrix = new Matrix3f();
    private Matrix3f gyroMatrix = new Matrix3f();
    private boolean gyrochange = false;

    private float fovy, ratio;
    private float focalLength;

    private float[] sensOri;

    public MyCamera(float ratio, float fovy_rad)
    {
        this.ratio = ratio;

        sensOri = MainActivity.getGyroData();

        setFov(fovy_rad);
    }

    public float getFov() {
        return fovy;
    }

    public void setFov(float fovy_rad) {
        this.fovy = fovy_rad;
        focalLength = 1.f / (float) tan(fovy_rad / 2);
    }

    public void scaleFov(float a_fovy_rad) {
        rotVelocity.set(0, 0);
        float t = Math.max(Math.min(fovy * a_fovy_rad, FOV_MAX_RAD), FOV_MIN_RAD);
        setFov(t);
    }

    public float getFovValue() {
        return (fovy - FOV_MIN_RAD) / (FOV_MAX_RAD - FOV_MIN_RAD);
    }

    public Vector3f project(Vector3f vec)
    {
        rotationMatrix.transform(vec);

        if (Options.gyrosens)
            gyroMatrix.transform(vec);

        if (Options.sphmode)
            vec.x = vec.x / ratio;

        else {
            float t = focalLength / (-vec.z);
            vec.x = vec.x * t / ratio;
            vec.y = vec.y * t;
        }

        return vec;
    }

    private Vector2f rotVelocity = new Vector2f();
    private Vector2f tmp = new Vector2f();

    public void touchRotate(float x, float y, double delta) {
        x = (float) atan(x * 2 * ratio / focalLength);
        y = (float) atan(y * 2 / focalLength);

        rotVelocity.set((float) (x / delta), (float) (y / delta));
    }

    private float rotX = 0;
    private float PIHalf = (float) (PI / 2);

    public void dampRotation(double delta)
    {
        //rotationMatrix.identity();
        if (Options.gyrosens) {
            gyroMatrix.rotateY(-(float) (sensOri[0] * delta));
            gyroMatrix.rotateLocal((float) (sensOri[1] * delta), 1, 0, 0);
            gyroMatrix.rotateLocal((float) (sensOri[2] * delta), 0, 0, -1);
            gyrochange = true;
        }

        else if (gyrochange)
        {
            gyrochange = false;
            gyroMatrix.identity();
        }


        //rotationMatrix.rotateZ((float) (sensOri[1] * delta));

        if (abs(rotVelocity.x) > Constants.CAM_MAXVEL)
            rotVelocity.x = signum(rotVelocity.x) * Constants.CAM_MAXVEL;

        if (abs(rotVelocity.y) > Constants.CAM_MAXVEL)
            rotVelocity.y = signum(rotVelocity.y) * Constants.CAM_MAXVEL;


        tmp.set(rotVelocity).mul((float) delta);

        if (abs(rotX + tmp.y) > PIHalf) {
            rotVelocity.y = 0;
            tmp.y = signum(tmp.y) * PIHalf - rotX;
        }

        rotate(tmp);
        rotVelocity.mul((float) pow(Constants.CAM_DAMPING, delta));
    }


    private Vector3f vec1 = new Vector3f();
    private Vector3f vec2 = new Vector3f();

    public void rotateTo(Vector3f to)
    {
        vec1.set(to);

        rotationMatrix.identity();

        MyRenderer.toCam(vec1);
        EarthRotControl.transform(vec1);

        vec2.set(vec1);

        float y_ang;
        if (vec1.z > 0) {
            y_ang = (float) (PI + atan(vec1.x / vec1.z));
            //Log.d("Camera", "BEHIND");
        }

        else
            y_ang = (float) atan(vec1.x / vec1.z);

        //Log.d("Camera", String.valueOf(vec2.x) + " "+ String.valueOf(vec2.z));
        //Log.d("Camera", String.valueOf(y_ang));
        rotationMatrix.rotateY(-y_ang);

        rotationMatrix.transform(vec2);

        //Log.d("Camera", String.valueOf(vec2.y) + " "+ String.valueOf(vec2.z));

        float x_ang = (float) atan(vec2.y / vec2.z); //dec

        //Log.d("Camera", String.valueOf(x_ang));

        rotX += x_ang;
        rotationMatrix.rotateLocal(x_ang, 1, 0, 0);
    }

    private void rotate(Vector2f vec) //vex.x is y axis and vec.y is x axis
    {
        rotX += vec.y;
        rotationMatrix.rotateY(vec.x);
        rotationMatrix.rotateLocal(vec.y, 1, 0, 0);
    }
}
