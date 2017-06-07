package com.example.borna2.trlababalan;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.annotation.RequiresApi;


public class MyGLSurfaceView3 extends GLSurfaceView implements SensorEventListener {
    MyGLRenderer renderer;
    private double previousX;
    private double previousY;
    Context mContext;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senMagnetometar;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MyGLSurfaceView3(Context context) {
        super(context);
        this.mContext = context;
        renderer = new MyGLRenderer(context);
        this.setRenderer(renderer);
        this.requestFocus();
        this.setFocusableInTouchMode(true);

        senSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senMagnetometar = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        senSensorManager.registerListener(this, senAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        senSensorManager.registerListener(this, senMagnetometar,
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);


    }

    float[] mGravity;
    float[] mGeomagnetic;

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                double currentX = - Math.toDegrees(orientation[1]);
                double currentY =  Math.toDegrees(orientation[2]);

                double deltaX, deltaY;
                deltaX = (currentX) - (previousX);
                deltaY = (currentY) - (previousY);
                renderer.angleX += deltaX;
                renderer.angleY += deltaY;

                previousX = currentX;
                previousY = currentY;

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}