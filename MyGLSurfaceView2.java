package com.example.borna2.trlababalan;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;


public class MyGLSurfaceView2 extends GLSurfaceView implements SensorEventListener {
    MyGLRenderer renderer;



    public static LinkedList<Double> graphDataRoll;
    public static LinkedList<Double> graphDataPitch;

    //definicija senzora
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;



    //Definicija varijabli za pospremanje očitanja senzora
    private long lastUpdate = 0;
    private float acc_x, acc_y, acc_z;

    //Definicija varijabli za izračun yaw
    private double XH;
    private double YH;

    private double previousX;
    private double previousY;
    Context mContext;


    public MyGLSurfaceView2(Context context) {
        super(context);
        this.mContext = context;
        renderer = new MyGLRenderer(context);
        this.setRenderer(renderer);

        this.requestFocus();
        this.setFocusableInTouchMode(true);


        graphDataRoll = new LinkedList<>();
        graphDataPitch = new LinkedList<>();

        senSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_GAME);


    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 10) {
                lastUpdate = curTime;

                acc_x = x;
                acc_y = y;
                acc_z = z;

                double roll = calcRoll(acc_y,acc_z);
                double pitch = calcPitch(acc_x,acc_y,acc_z);

                double currentX = roll;
                double currentY = pitch;
                double deltaX, deltaY;
                deltaX = (currentX * 57.2958) - (previousX * 57.2958); //prebacio u stupnjeve
                deltaY = (currentY * 57.2958) - (previousY * 57.2958);
                renderer.angleX += deltaX;
                renderer.angleY += deltaY;

                previousX = currentX;
                previousY = currentY;

                graphDataRoll.add(currentX * 57.2958);
                graphDataPitch.add(currentY * 57.2958);

            }

        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private double calcRoll(double acc_y, double acc_z) {
        return Math.atan2(acc_y,acc_z);

    }

    private double calcPitch(double acc_x, double acc_y ,double acc_z) {
        return Math.atan2((-acc_x),Math.sqrt(acc_y * acc_y + acc_z*acc_z));

    }

}